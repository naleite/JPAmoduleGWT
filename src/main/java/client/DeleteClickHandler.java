package client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

/**
 * Created by naleite on 14/12/24.
 */
public class DeleteClickHandler implements ClickHandler {

    private String id;

    public DeleteClickHandler(String id){
        this.id=id;
    }
    @Override
    public void onClick(ClickEvent clickEvent) {
        RequestBuilder rb=new RequestBuilder(RequestBuilder.DELETE, GWT
                .getHostPageBaseURL() +"rest/ev/"+id);
        Window.confirm("Are you sure to remove Evenement " + id + ".");

        rb.setCallback(new RequestCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {

                if(response.getText().equals("ok")){
                    Window.alert("Evenement "+id+" has been deleted.");
                    GwtMain.refreshList();
                }
                else{
                    Window.alert("Evenement "+id+" has NOT been deleted.");
                }
            }

            @Override
            public void onError(Request request, Throwable throwable) {

            }
        });
        try{
            rb.send();
        }
        catch (Exception e){}
        finally {

        }

    }
}
