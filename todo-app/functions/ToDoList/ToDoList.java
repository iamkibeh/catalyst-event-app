import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStreamReader;
 
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
 
import com.catalyst.advanced.CatalystAdvancedIOHandler;
import com.zc.common.ZCProject;
import com.zc.component.object.ZCObject;
import com.zc.component.object.ZCRowObject;
import com.zc.component.object.ZCTable;
import com.zc.component.zcql.ZCQL;
 
public class ToDoList implements CatalystAdvancedIOHandler {
 private static final String GET = "GET";
 private static final String POST = "POST";
 private static final String DELETE = "DELETE";
 private JSONObject responseData = new JSONObject();
 private static final Logger LOGGER = Logger.getLogger(ToDoList.class.getName());
 
@SuppressWarnings("unchecked")
@Override
 public void runner(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
		ZCProject.initProject();
		String uri = request.getRequestURI();
		String method = request.getMethod();
		//GET method gets data from the TodoItems table in the Data Store.
		
		if (method.equals(GET) && uri.equals("/all")) {
		System.out.println("Inside GET " + uri);
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		
		Integer perPage = Integer.parseInt(request.getParameter("perPage"));
		
		Integer totalTodos = Integer	
								.parseInt(ZCQL.getInstance().executeQuery("SELECT COUNT(ROWID) FROM TodoItems").get(0)
								.get("TodoItems", "ROWID").toString());
		
		Boolean hasMore = totalTodos > page * perPage;
		
		
		ArrayList<HashMap<String, String>> todoItems = new ArrayList<>();
		
		// String query = "SELECT ROWID, Notes FROM TodoItems LIMIT %d,%d";
		ZCQL.getInstance().executeQuery(String.format("SELECT ROWID, Notes FROM TodoItems LIMIT %d,%d",
		
		(page - 1) * perPage + 1, perPage)).forEach(row -> {
		
		// HashMap<String, String> todoListMap =	new HashMap<>();
		// todoListMap.put("id", row.get("TodoItems", "ROWID").toString());
		// todoListMap.put("notes", row.get("TodoItems", "Notes").toString());
		// todoItems.add(todoListMap);
		
		todoItems.add(new HashMap<>() {
		{
		put("id", row.get("TodoItems", "ROWID").toString());
		put("notes", row.get("TodoItems", "Notes").toString());
		}
		});
		
		});
		
		response.setStatus(200);
		
		
		
		responseData.put("status", "success");
		
		responseData.put("data", new JSONObject() {
		{
		put("hasMore", hasMore);
		put("todoItems", todoItems);
		}
		
		});
		//POST method sends data to persist in the TodoItems table in the Data Store
		
		} else if (method.equals(POST) && uri.equals("/add")) {
		
		JSONParser jsonParser = new JSONParser();
		
		ServletInputStream requestBody = request.getInputStream();
		
		
		JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(requestBody, "UTF-8"));
		
		
		String notes = jsonObject.get("notes").toString();
		
		
		ZCRowObject row = ZCRowObject.getInstance();
		
		row.set("Notes", notes);
		
		
		ZCRowObject todoItem = ZCObject.getInstance().getTable("TodoItems").insertRow(row);
		
		
		response.setStatus(200);
		
		responseData.put("status", "success");
		
		responseData.put("data", new JSONObject() {
		
		{
		
		put("todoItem", new JSONObject() {
		
		{
		
		put("id", todoItem.get("ROWID").toString());
		
		put("notes", todoItem.get("Notes").toString());
		
		}
		
		});
		
		}
		
		});
		//Delete method deletes the selected items from the Data Store
		
		} else if (method.equals(DELETE)) {
		
		ZCTable table = ZCObject.getInstance().getTable("TodoItems");
		
		
		table.deleteRow(Long.parseLong(uri.substring(1)));
		
		
		response.setStatus(200);
		
		responseData.put("status", "success");
		
		responseData.put("data", new JSONObject() {
		
		{
		
		put("todoItem", new JSONObject() {
		
		{
		
		put("id", uri.substring(1));
		
		}
		
		});
		
		
		}
		
		});
		
		
		} else {
		
		response.setStatus(404);
		
		responseData.put("status", "failure");
		
		responseData.put("message", "Please check if the URL trying to access is a correct one");
		
		}
		
		
		
		} catch (Exception e) {
		
		LOGGER.log(Level.SEVERE, "Exception in Main", e);
		
		responseData.put("status", "failure");
		
		responseData.put("message", "We're unable to process the request.");
		
		
		}
 
 
	response.setContentType("application/json");
	
	response.getWriter().write(responseData.toString());
	
 }
 
}