package client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Button;



/**
 * Created by naleite on 14/12/25.
 */
public class AddCommentClickHandler implements ClickHandler {

    private String eid;
    private FormPanel formPanel=new FormPanel();
    private TextArea commentaireTextArea=new TextArea();
    private TextBox pidTextBox=new TextBox();
    private Button submitBtm=new Button("Submit");
    private PopupPanel mainPanel=new PopupPanel(true);
    private RequestBuilder rb;

    public AddCommentClickHandler(String eid){

        this.eid=eid;

    }
    @Override
    public void onClick(ClickEvent clickEvent) {
        init();
        mainPanel.show();

    }

    private void init() {
        pidTextBox.setText("Your ID");
        pidTextBox.setName("id");
        pidTextBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                pidTextBox.setText("");
            }
        });
        commentaireTextArea.setText("Write your comment here.");
        commentaireTextArea.setPixelSize(200, 100);
        commentaireTextArea.setName("cmt");
        commentaireTextArea.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                commentaireTextArea.setText("");
            }
        });
        submitBtm.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                formPanel.submit();
                //mainPanel.hide();
            }
        });

        Label l=new Label("Add comment for Evenement "+eid);
        VerticalPanel vp=new VerticalPanel();
        vp.add(l);
        vp.add(pidTextBox);
        vp.add(commentaireTextArea);
        vp.setSpacing(10);
        //formPanel.add(pidTextBox);
        //formPanel.add(commentaireTextArea);
        vp.add(submitBtm);
        formPanel.setWidget(vp);
        formPanel.setMethod(FormPanel.METHOD_POST);
        mainPanel.setPopupPosition(Window.getClientWidth() / 2 - 100, Window.getClientHeight() / 2 - 100);
        mainPanel.setGlassEnabled(true);
        mainPanel.setWidget(formPanel.asWidget());

        formPanel.addSubmitHandler(new FormPanel.SubmitHandler() {
            @Override
            public void onSubmit(FormPanel.SubmitEvent submitEvent) {
                if (pidTextBox.getText() == "Your ID" || pidTextBox.getText() =="") {
                    Window.alert("Your ID is empty.");
                    submitEvent.cancel();
                }
                else if (commentaireTextArea.getText().trim() == ""||commentaireTextArea.getText().equals("Write your comment here.")) {
                    Window.alert("Your Comment is empty.");
                    submitEvent.cancel();
                }
                else{
                    formPanel.setAction("/rest/com/"+pidTextBox.getText()+"-"+eid);
                }
            }
        });
        formPanel.setTitle("cmt");
        formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent submitCompleteEvent) {
                //rb=new RequestBuilder(RequestBuilder.POST, GWT
                  //      .getHostPageBaseURL() +"rest/com/"+pidTextBox.getText()+"-"+eid);

                Window.alert("Comment successfully!");
                mainPanel.hide();

            }
        });
    }
}
