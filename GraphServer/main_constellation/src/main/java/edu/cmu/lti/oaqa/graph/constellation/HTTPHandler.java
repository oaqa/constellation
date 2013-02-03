package edu.cmu.lti.oaqa.graph.constellation;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class HTTPHandler extends HttpServlet /*AbstractHandler*/
{
	@Override
    public void doPost(HttpServletRequest request,HttpServletResponse response) 
        throws IOException, ServletException
    {
		response.setContentType("text/html;charset=utf-8");
        BufferedReader reader = request.getReader();
        String inputLine = "";
        while((inputLine += reader.readLine()) != null){
        	break;
        }
        //inputLine = "{\"jsonrpc\":\"2.0\",\"id\":\"req-id-01\", "+inputLine.substring(1);
        System.out.println("input: "+inputLine);
        //JsonServer jsonserver = new JsonServer();
        JsonServer2 jsonserver = new JsonServer2();
        String outputLine = jsonserver.runJsonQuery(inputLine);
        response.setStatus(HttpServletResponse.SC_OK);
        //baseRequest.setHandled(true);
        System.out.println("output: "+outputLine);
        response.getWriter().println(outputLine);
    }
}