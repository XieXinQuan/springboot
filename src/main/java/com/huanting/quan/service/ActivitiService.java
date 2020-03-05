package com.huanting.quan.service;

import com.huanting.quan.Enum.CommonEnum;
import com.huanting.quan.Enum.Constant;
import com.huanting.quan.Enum.NoticeType;
import com.huanting.quan.Enum.ResultEnum;
import com.huanting.quan.entity.Member;
import com.huanting.quan.exception.GlobalException;
import com.huanting.quan.repository.MemberRepository;
import com.huanting.quan.repository.UserRepository;
import com.huanting.quan.util.DateUtil;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/2/4
 */
@Service
public class ActivitiService extends BaseService{
    @Resource
    RuntimeService runtimeService;
    @Resource
    TaskService taskService;
    @Resource
    RepositoryService repositoryService;
    @Resource
    IdentityService identityService;
    @Resource
    HistoryService historyService;

    @Resource
    MemberRepository memberRepository;
    @Resource
    UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(ActivitiService.class);

    /**
     * 申请会员
     * @throws Exception
     */
    public String vipApplication() throws Exception {
        //判断当时用户是否已经是会员
        Member isMember = memberRepository.findAllByUserId(getCurrentUserId());
        if (isMember != null){
            throw new GlobalException(ResultEnum.CustomException.getKey(), "您已经在 " + DateUtil.dateTimeFormat(isMember.getCreateTime()) + " 成为了会员.");
        }

        //此处不能省略-- 否则startBy中无数据
        identityService.setAuthenticatedUserId(getCurrentUserId().toString());

        //判断是否已申请
        if (isExistsVipApplication()){
            throw new GlobalException(ResultEnum.CustomException.getKey(), "你已经提交过会员申请了, 请等待审核..");
        }


        Map<String, Object> applicantMap = new HashMap<>(2);
        // 设置申请人
        applicantMap.put("applicant", getCurrentUserId());
        //设置审批人
        applicantMap.put("Approver", Constant.adminUserId);

        //开启myProcess审批流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess", applicantMap);
        //申请人 完成申请
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        taskService.complete(task.getId());

        logger.info("userId:{}, userName:{} 申请成为会员...", getCurrentUserId(), getCurrentUserName());



        return "Success";
    }

    /**
     * 会员进度图查看
     * @return
     */
    public InputStream vipApplicationViewProcess(String processInstanceId){

        if (processInstanceId == null) {
            //查看我的申请 activiti 实例Id
            List<ProcessInstance> processInstances = myStartProcessInstance();

            if (listIsEmpty(processInstances)) {
                throw new GlobalException(ResultEnum.CustomException.getKey(), "你还没有提交过会员申请.");
            }
            processInstanceId = processInstances.get(0).getId();
        }
        return process(processInstanceId);

    }

    @Transactional(rollbackFor=Exception.class)
    public void agreeVipApplication(String processInstanceId, Integer status){

        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        if (!getCurrentUserId().toString().equals(task.getAssignee())){
            throw new GlobalException(ResultEnum.CustomException.getKey(), "对不起,您没有审批权限!");
        }

        taskService.complete(task.getId());
        if (CommonEnum.Normal.getKey().equals(status)){

            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).finished().list();
            List<HistoricActivityInstance> collect = list.stream().filter(historicActivityInstance -> !"startEvent".equals(historicActivityInstance.getActivityType()))
                    .filter(historicActivityInstance -> !getCurrentUserId().toString().equals(historicActivityInstance.getAssignee()))
                    .collect(Collectors.toList());

            if (listIsEmpty(list)){
                throw new GlobalException(ResultEnum.CustomException.getKey(), "审批异常!");

            }
            HistoricActivityInstance historicActivityInstance = collect.get(0);
            Member member = new Member();
            member.setIntegral(0.0f);
            member.setStatus(1);
            member.setUserId(Long.parseLong(historicActivityInstance.getAssignee()));
            memberRepository.save(member);
        }
    }

    /**
     * 加载我需要审批的任务 转换发送给前端
     * @return
     */
    public List<Map<String, Object>> loadNeedMyApproveTask(){
        List<Task> tasks = needMyApproveTask();
        if (listIsEmpty(tasks)) {
            return null;
        }

//        historyService.createHistoricTaskInstanceQuery().
        List<Map<String, Object>> list = new ArrayList<>();
        tasks.stream().forEach(task -> {
            String processInstanceId = task.getProcessInstanceId();
            Map<String, Object> map = new HashMap<>();
            map.put("id", processInstanceId);
            map.put("time", task.getCreateTime());
            map.put("type", NoticeType.NeedApprove.getKey());
            map.put("content", "Need Approve");

            List<HistoricActivityInstance> list1 = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).finished().list();

            for (HistoricActivityInstance historicActivityInstance : list1){
                String assignee = historicActivityInstance.getAssignee();
                if (!"startEvent".equals(historicActivityInstance.getActivityType()) && assignee != null){
                    map.put("name", userRepository.findAllById(Long.parseLong(assignee)).getName());
                    list.add(map);
                }
            }
        });
        return list;
    }

    /**
     * 加载我需要审批的任务
     * @return
     */
    private List<Task> needMyApproveTask(){
        List<Task> list = taskService.createTaskQuery().taskAssignee(getCurrentUserId().toString()).list();
        if (listIsEmpty(list)) {
            return null;
        }
        return list;
    }

    private List<ProcessInstance> myStartProcessInstance(){
        return runtimeService.createProcessInstanceQuery().startedBy(getCurrentUserId().toString()).list()
                .stream().filter(processInstance -> Constant.memberProcess.equals(processInstance.getProcessDefinitionKey()))
                .collect(Collectors.toList());
    }



    private boolean isExistsVipApplication(){
        List<ProcessInstance> processInstances = myStartProcessInstance();
        if (listIsEmpty(processInstances)) {
            return false;
        }

        return processInstances.stream()
                .filter(processInstance -> Constant.memberProcess.equals(processInstance.getProcessDefinitionKey()))
                .collect(Collectors.toList()).size() > 0;
    }
    private boolean listIsEmpty(List list){
        return list == null || list.size() == 0;
    }





    public void applicant() throws Exception {
        Map<String, Object> map = new HashMap<>(1);
        //流程图的${applicant}
        map.put("applicant", 6L);

        //启动
        identityService.setAuthenticatedUserId("6");
        ExecutionEntity pi = (ExecutionEntity)runtimeService.startProcessInstanceByKey("myProcess", map);
        logger.info("流程启动成功!");

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        printTaskInformation(task);

        Thread.sleep(2000L);
//        Map<String, Object> map2 = new HashMap<>(1);
//        map2.put("Approver", 15L);
//        taskService.complete(task.getId(), map2);
//
//        task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
//        printTaskInformation(task);

    }
    public void loadMyTask(){
        List<Task> list = taskService.createTaskQuery().taskAssignee("15").list();
        if (list != null && list.size() == 0) {
            return;
        }



        list.stream().forEach(task -> {
            try {
                printTaskInformation(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public void loadMyStartTask(){
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().startedBy("6").list();

        for (ProcessInstance processInstance : list){
            logger.info("process id: {}", processInstance.getId());
            List<Task> list1 = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
            if (list1 == null || list1.size() == 0) {
                return;
            }
            list1.stream().forEach(task -> {
                try {
                    printTaskInformation(task);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }



    private void printTaskInformation(Task task) throws Exception {
        if (task == null) {
            return;
        }
        logger.info("任务Id: {}", task.getId());
        logger.info("任务的办理人: {}", task.getAssignee());
        logger.info("任务名称: {}", task.getName());
        logger.info("任务发起时间: {}", DateUtil.dateTimeFormat(task.getCreateTime()));
    }

    public InputStream process(String processInstanceId){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        String processInstanceId = "10001";
        //获取流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        String processDefinitionId = StringUtils.EMPTY;
        if (processInstance == null) {
            //查询已经结束的流程实例
            HistoricProcessInstance processInstanceHistory =
                    historyService.createHistoricProcessInstanceQuery()
                            .processInstanceId(processInstanceId).singleResult();
            if (processInstanceHistory == null) {
                return null;
            } else {
                processDefinitionId = processInstanceHistory.getProcessDefinitionId();
            }
        } else {
            processDefinitionId = processInstance.getProcessDefinitionId();
        }

        //使用宋体
        String fontName = "宋体";
        //获取BPMN模型对象
        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        //获取流程实例当前的节点，需要高亮显示
        List<String> currentActs = Collections.EMPTY_LIST;
        if (processInstance != null) {
            currentActs = runtimeService.getActiveActivityIds(processInstance.getId());
        }


        return processEngine.getProcessEngineConfiguration()
                .getProcessDiagramGenerator()
                .generateDiagram(model, "png", currentActs, new ArrayList<String>(),
                        fontName, fontName, fontName, null, 1.0);
//        return null;
    }































    /**
     * 启动流程
     */
    public void startProcess(){
        ProcessInstance myProcess = runtimeService.startProcessInstanceByKey("myProcess");

        List<Task> list = taskService.createTaskQuery().asc().list();

        for (Task task : list){
            System.out.println("...........:"+task.getAssignee());
        }



    }

    /**
     * 发起申请
     * @return
     */
    public String commitApproval(){
        logger.info("调用流程存储服务，已部署流程数量：{}", repositoryService.createDeploymentQuery().count());

        Map<String, Object> map = new HashMap<>();
        //流程图的${applicant}
        map.put("applicant", 6L);

        //流程启动
        identityService.setAuthenticatedUserId("6L");
        // 指定流程的发起者 不指定发起者的字段就为空，注意跟审核人分开
        ExecutionEntity pi = (ExecutionEntity) runtimeService.startProcessInstanceByKey("myProcess");
        logger.info("启动流程成功！");

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        logger.info("任务ID: "+task.getId());
        logger.info("任务的办理人: "+task.getAssignee());
        logger.info("任务名称: "+task.getName());
        logger.info("任务的创建时间: "+task.getCreateTime());
        logger.info("流程实例ID: "+task.getProcessInstanceId());
//
//
//        // 开启后，环节会走到发起请假请求，要完成这个环节，才能到下一个审核环节
        taskService.complete(task.getId());

//        queryApplication();
//
//        completeApplication();
        return "success";
    }


    /**
     * 查询流程
     */
    public void queryApplication(){
        //创建查询对象
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        query.orderByProcessDefinitionId().asc();

        //通过key获取
        query.processDefinitionKey("myProcess");
        //执行查询获取流程定义明细
        List<ProcessDefinition> list = query.list();

        list.stream().forEach(processDefinition -> logger.info(processDefinition.getId()));

    }

    public void completeApplication(){
        List<Task> list = taskService.createTaskQuery().list();


        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        Task task = Optional.ofNullable(list).map(tasks -> tasks.get(0)).orElse(null);

        if (task!= null) defaultProcessEngine.getTaskService().complete(task.getId());

    }
}
