package bysj.controller.basic.department;

import bysj.domain.Degree;
import bysj.domain.Department;
import bysj.service.DegreeService;
import bysj.service.DepartmentService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * 将所有方法组织在一个Controller(Servlet)中
 */
@WebServlet("/department.ctl")
public class DepartmentController extends HttpServlet {

    /**
     * POST,http://49.233.189.50:8080/JW_201802104068_25/index.html
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //根据request对象，获得代表参数的JSON字串
        String department_json = JSONUtil.getJSON(request);

        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        System.out.println(departmentToAdd);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //增加加Department对象
            DepartmentService.getInstance().add(departmentToAdd);
            message.put("message", "增加成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * DELETE,http://49.233.189.50:8080/JW_201802104068_25/index.html
     * 删除一个对象：根据来自前端请求的id，删除数据库表中id的对应记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try{
            //到数据库表中删除
            DepartmentService.getInstance().delete(id);
            message.put("message", "删除成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }


    /**
     * PUT,http://49.233.189.50:8080/JW_201802104068_25/index.html
     * 修改对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try{
            //增加加Department对象
            DepartmentService.getInstance().update(departmentToAdd);
            message.put("message", "更新成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * GET,http://49.233.189.50:8080/JW_201802104068_25/index.html
     * 响应一个或所有对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //读取参数id
        String id_str = request.getParameter("id");
        String type = request.getParameter("paraType");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try{
        //如果id = null, 表示响应所有对象，否则响应id指定的对象
        if(id_str != null && type == null){
            int id = Integer.parseInt(id_str);
            responseDepartment(id, response);
        }else if(id_str != null && type != null){
            int schoolId = Integer.parseInt(id_str);
            responseDepartmentBySchool(schoolId,response);
        } else{
            responseDepartment(response);
        }
    }catch (Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    //响应一个对象
    private void responseDepartment(int id, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            //根据id查找
            Department department = DepartmentService.getInstance().find(id);
        String department_json = JSON.toJSONString(department);
        //响应
        //响应到前端
        response.getWriter().println(department_json);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private void responseDepartmentBySchool(int schoolId,HttpServletResponse response) throws  IOException{
        Collection<Department> departments = DepartmentService.getInstance().findAllBySchool(schoolId);
        String department_json = JSON.toJSONString(departments);
        //响应到前端
        response.getWriter().println(department_json);
    }
    //响应所有对象
    private void responseDepartment(HttpServletResponse response)
            throws ServletException, IOException {
        //获得所有
        Collection<Department> departments = DepartmentService.getInstance().findAll();
        String department_json = JSON.toJSONString(departments);
        //响应到前端
        response.getWriter().println(department_json);
    }

}
