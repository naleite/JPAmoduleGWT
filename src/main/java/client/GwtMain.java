package client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.*;

/**
 * Created by naleite on 14/12/22.
 */
public class GwtMain implements EntryPoint{
    /**
     * This is the entry point method.
     */
    private VerticalPanel mainPanel = new VerticalPanel();
    private static FlexTable stocksFlexTable = new FlexTable();
    private HorizontalPanel addPanel = new HorizontalPanel();
    private TextBox pidTextBox = new TextBox();
    private TextBox departTextBox = new TextBox();
    private TextBox destTextBox = new TextBox();
    private Button addButton = new Button("addEvent");

    //add participant
    private Button addPButton;
    private TextBox pidTextBoxForAddP,eidTextBoxForAddP;
    private VerticalPanel panelForAddP;

    private final String URLBASE="rest/";

    private Label lastUpdatedLabel = new Label();

    public void onModuleLoad() {


        Button refreshBtn=new Button("Refresh");
        refreshBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                refreshList();
            }
        });


        stocksFlexTable.setText(0, 0, "ID");
        stocksFlexTable.setText(0, 1, "Depart");
        stocksFlexTable.setText(0, 2, "Dest");
        stocksFlexTable.setText(0, 3, "Conducteur");
        stocksFlexTable.setText(0, 4, "Places restes");
        stocksFlexTable.setText(0, 5, "Edit");
        stocksFlexTable.setText(0, 6, "Remove");
        stocksFlexTable.setText(0, 7, "Comment");
        stocksFlexTable.setText(1,0,"Chargement en cours");




        stocksFlexTable.getRowFormatter().addStyleName(0,"tableEventsRow0");
        stocksFlexTable.addStyleName("tableEvents");
        RootPanel.get().add(refreshBtn);

        Label label1=new Label("Add an event(your ID, your departure, your destination) :");

        pidTextBox.setText("ID Conducteur");
        pidTextBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                pidTextBox.setText("");
            }
        });

        departTextBox.setText("Ville de depart");
        departTextBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                departTextBox.setText("");
            }
        });

        destTextBox.setText("Ville d'arrive");
        destTextBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                destTextBox.setText("");
            }
        });




        // Assemble Add Stock panel.
        addPanel.add(pidTextBox);
        addPanel.add(departTextBox);
        addPanel.add(destTextBox);
        addPanel.add(addButton);
        addPanel.setSpacing(10);

        addPanel.addStyleName("addPanel");
        // Assemble Main panel.
        mainPanel.add(stocksFlexTable);
        mainPanel.add(label1);
        mainPanel.add(addPanel);
        mainPanel.add(lastUpdatedLabel);
        mainPanel.setSpacing(10);

        RootPanel.get().add(mainPanel);



        //add an event
        addButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, GWT
                        .getHostPageBaseURL() + URLBASE+
                        "per/"+pidTextBox.getText()+"-"+
                                departTextBox.getText()+"-"+
                                destTextBox.getText());
                rb.setCallback(new PostCallback());
                try{
                    rb.send();
                    //Window.alert(rb.getHTTPMethod()+", "+rb.getUrl());
                }
                catch (Exception e){}
            }

        });


        addPButton=new Button("Take it");
        pidTextBoxForAddP=new TextBox();
        eidTextBoxForAddP=new TextBox();
        pidTextBoxForAddP.setMaxLength(5);
        eidTextBoxForAddP.setMaxLength(5);
        pidTextBoxForAddP.setText("Your ID");
        eidTextBoxForAddP.setText("Evenement ID");
        panelForAddP=new VerticalPanel();


        panelForAddP.add(pidTextBoxForAddP);
        panelForAddP.add(eidTextBoxForAddP);
        panelForAddP.add(addPButton);
        panelForAddP.setSpacing(10);


        pidTextBoxForAddP.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                pidTextBoxForAddP.setText("");
            }
        });

        eidTextBoxForAddP.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                eidTextBoxForAddP.setText("");
            }
        });




        Label addPLabel=new Label("Add yourself to an Evenement.");
        RootPanel.get().add(addPLabel);
        RootPanel.get().add(panelForAddP);
        ;


        refreshList();


        addPButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                int pids=Integer.parseInt(pidTextBoxForAddP.getText());
                int eids=Integer.parseInt(eidTextBoxForAddP.getText());

                Window.confirm("Are you sure?" + pids + " " + eids);

                RequestBuilder rb1=new RequestBuilder(RequestBuilder.POST, GWT
                        .getHostPageBaseURL() + "rest/ev/" + eids + "-" + pids);

                rb1.setCallback(new RequestCallback() {
                    @Override
                    public void onResponseReceived(Request request, Response response) {

                        if(200==response.getStatusCode()){

                            if(response.getText()=="nok"){
                                Window.alert("You are NOT added.");
                            }
                            else{
                                Window.alert(response.getText());
                            }
                            refreshList();
                        }
                        else{
                            Window.alert(response.getStatusCode() + response.getHeadersAsString());
                        }
                    }

                    @Override
                    public void onError(Request request, Throwable throwable) {
                        Window.alert("ERROR"+throwable.getMessage() );
                    }
                });
                try{
                    rb1.send();
                }
                catch (Exception e){}
                finally {

                }

            }
        });
    }//fin onModuleLoad


    public static void refreshList() {
            RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, GWT
                    .getHostPageBaseURL() + "rest/");
            rb.setTimeoutMillis(10000);
            rb.setCallback(new RequestCallback() {


                public void onError(Request request, Throwable exception) {
                    Window.alert(exception.getMessage());
                }

                public void onResponseReceived(Request request,
                                               Response response) {
                    if (200 == response.getStatusCode()) {

                        JSONValue res = JSONParser.parseStrict(response.getText());
                        JSONArray array = res.isArray();
                        //Window.alert("size:" + array.size());
                        for(int i=1;i<stocksFlexTable.getRowCount();i++){
                            stocksFlexTable.removeRow(i);
                        }
                        for (int i = 0; i < array.size(); i++) {

                            //JSONObject jo= (JSONObject) array.get(Integer.parseInt(area.getValue())-1);

                            JSONObject jo = (JSONObject) array.get(i);
                            //stocksFlexTable.addCell(i+1);
                            //String s="ID:"+jo.get("id")+", Depart de: "+jo.get("villeDepart")+" a "+jo.get("villeDest")+",Conduicteur: "+jo.get("conducteur").isObject().get("nom")+".";
                            Button removeButton=new Button("Delete");
                            Button detailButton=new Button("Detail");
                            Button commentButton=new Button("Add Comment");

                            stocksFlexTable.setText(i + 1, 0, jo.get("id").toString());
                            stocksFlexTable.setText(i + 1, 1, eleverGuillemets(jo.get("villeDepart").toString()));
                            stocksFlexTable.setText(i + 1, 2, eleverGuillemets(jo.get("villeDest").toString()));
                            stocksFlexTable.setText(i + 1, 3, eleverGuillemets(jo.get("conducteur").isObject().get("nom").toString()));
                            stocksFlexTable.setText(i + 1, 4, jo.get("nbPlaceReste").toString());
                            stocksFlexTable.setWidget(i+1,5,detailButton);
                            stocksFlexTable.setWidget(i+1,6,removeButton);
                            stocksFlexTable.setWidget(i+1,7,commentButton);

                            DetailClickHandler clickHandler= new DetailClickHandler(stocksFlexTable.getText(i + 1, 0) + "");
                            clickHandler.setPositionPopup(detailButton.getAbsoluteLeft(),detailButton.getAbsoluteTop());
                            detailButton.addClickHandler(clickHandler);
                            DeleteClickHandler deleteClickHandler=new DeleteClickHandler(stocksFlexTable.getText(i+1,0));
                            removeButton.addClickHandler(deleteClickHandler);
                            AddCommentClickHandler addCommentClickHandler=new AddCommentClickHandler(stocksFlexTable.getText(i+1,0));
                            commentButton.addClickHandler(addCommentClickHandler);



                        }
                        setStyleForFlexTable(stocksFlexTable);
                        Window.alert("Load successfully");

                    } else {
                        Window.alert("Load Failed. "+response.getStatusCode() + "");
                    }
                }
            });
            try {
                rb.send();
            } catch (RequestException e) {
                e.printStackTrace();
            }


    }

    public static void setStyleForFlexTable(FlexTable table) {
        int nbrow=table.getRowCount();
        //System.out.println("nbrow:"+nbrow);
        //Window.alert(nbrow+","+stocksFlexTable.getCellCount(1));
        for(int i=0;i<nbrow;i++){
            int count=table.getCellCount(i);
            for(int j=0;j<count;j++){
                table.getCellFormatter().addStyleName(i,j,"EventListColumn");
                //System.out.println("("+i+","+j+")");
            }
        }
    }


    public static String eleverGuillemets(String s){

        int len=s.length();
        return s.substring(1,len-1);
    }

}

