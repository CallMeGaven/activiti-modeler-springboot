package org.activiti.explorer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONArray;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.activiti.explorer.conf.ActivitiEngineConfiguration;
//import org.activiti.explorer.conf.DemoDataConfiguration;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@CrossOrigin
@SpringBootApplication
public class AwsApplication {
	@Autowired
	RepositoryService repositoryService;
    @Autowired
    ProcessEngine processEngine;

    protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AwsApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(AwsApplication.class, args);
	}

//    @Bean
//    public EmbeddedServletContainerFactory servletContainer() {
//        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint constraint = new SecurityConstraint();
//                constraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                constraint.addCollection(collection);
//                context.addConstraint(constraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(httpConnector());
//        return tomcat;
//    }
//
//    @Bean
//    public Connector httpConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        //Connector监听的http的端口号
//        connector.setPort(9090);
//        connector.setSecure(false);
//        //监听到http的端口号后转向到的https的端口号
//        connector.setRedirectPort(9091);
//        return connector;
//    }

	@RequestMapping("/bs")
	public String getIndex(Model model)
	{
		System.out.println("/bs");
		return "modeler";
	}

	@RequestMapping("/bs1")
    @ResponseBody
	public String getIndexA(Model model)
	{
		System.out.println("/bs");
		return "haha";
	}
	@RequestMapping("/bs2")
	public String getIndexB(Model model)
	{
		System.out.println("/bs2");
		return "index";
	}

	/**
	 * 创建模型
	 */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public void create(@RequestParam("name") String name, @RequestParam("key") String key, @RequestParam("description") String description,
                       HttpServletRequest request, HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            org.activiti.engine.repository.Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            description = StringUtils.defaultString(description);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(name);
            modelData.setKey(StringUtils.defaultString(key));

            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
        } catch (Exception e) {

        }
    }
    /**
     * 模型列表
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public JSONArray modelList() {

        List<org.activiti.engine.repository.Model> list = repositoryService.createModelQuery().list();
        List<String> listMeteInfo = new ArrayList<String>();
        for(org.activiti.engine.repository.Model model:list){
            listMeteInfo.add( model.getMetaInfo() );
        }
        JSONArray json = JSONArray.fromObject(listMeteInfo);
       //把json转换为String
        return  json;
    }
    /**
     * 根据Model部署流程
     * @param modelId 为model表里的ID_列
     */
    @RequestMapping(value = "deploy/{modelId}")
    @ResponseBody
    public String deploy(@PathVariable("modelId") String modelId, RedirectAttributes redirectAttributes) {
        String info = "";
        try {
            org.activiti.engine.repository.Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes = null;

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes)).deploy();
            LOGGER.info("部署成功，部署ID=" + deployment.getId());
            info = "部署成功，部署ID=" + deployment.getId();
        } catch (Exception e) {
            LOGGER.error("根据模型部署流程失败：modelId={}", modelId, e);
            info = "根据模型部署流程失败:" + e;
        }
        return info;
    }


    /**
     * 挂起、激活流程实例
     */
    @RequestMapping(value = "processdefinition/update/{state}/{processDefinitionId}")
    public String updateState(@PathVariable("state") String state, @PathVariable("processDefinitionId") String processDefinitionId,
                              RedirectAttributes redirectAttributes) {
        if (state.equals("active")) {
            redirectAttributes.addFlashAttribute("message", "已激活ID为[" + processDefinitionId + "]的流程定义。");
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
        } else if (state.equals("suspend")) {
            repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
            redirectAttributes.addFlashAttribute("message", "已挂起ID为[" + processDefinitionId + "]的流程定义。");
        }
        return "redirect:/workflow/process-list";
    }

    /**
     * 启动流程实例
     * processDefinitionId 为act_re_procdef表ID列
     * depelament列为部署表的ID列
     */
    @RequestMapping(value = "startProcessInstance/{processDefinitionId}")
    @ResponseBody
    public String startProcessIn(@PathVariable("processDefinitionId")String processDefinitionId ){
        // Create Activiti process engine
        String info = "";
        // Get Activiti services
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        try
        {
            runtimeService.startProcessInstanceById(processDefinitionId);
            LOGGER.info("启动流程===》ID====》"+processDefinitionId);
            info = "启动流程===》ID====》"+processDefinitionId;
        }
        catch (Exception e)
        {
            LOGGER.info("启动流程失败===》ID====》" + e);
            info = "启动流程失败===》ID====》" + e;
        }
        return info;
    }

    /*
    * management
    * Admin
    * Engineering
    * Marketing
    * Sales
    * User
    * */
    @RequestMapping(value = "getTask/{nameOrGroup}")
    @ResponseBody
    public JSONArray GetTaskByNameOrGroup(@PathVariable("nameOrGroup") String nameOrGroup){
        TaskService taskService = processEngine.getTaskService();
        List<String> taskList = new ArrayList<String>();
        List<Task> tasks = taskService.createTaskQuery().list(); //.taskCandidateGroup(nameOrGroup).list();
        for(Task t:tasks){
            taskList.add ("{\"name\":\"" + t.getName() + "\",\"id\":\"" + t.getId() + "\"}");
        }
        JSONArray json = JSONArray.fromObject(taskList);
        LOGGER.info(json.toString());
        return json;
    }

        /*
        * 完成任务
        *
        * */
    @RequestMapping(value = "complete/{id}")
    @ResponseBody
    public String Complete( @PathVariable("id") String id){

        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().list();
        Task task = tasks.get( Integer.parseInt(id) );
        Map<String, Object> taskVariables = new HashMap<String, Object>();
        try
        {
            taskService.complete(task.getId());
        }
        catch (Exception e)
        {
            LOGGER.info("错误信息"+ e);
        }

        return "ok";
    }
}
