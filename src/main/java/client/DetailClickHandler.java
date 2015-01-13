package client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.impl.WindowImpl;
import com.google.gwt.user.client.ui.*;

/**
 * Created by naleite on 14/12/24.
 */
public class DetailClickHandler implements ClickHandler {
    RequestBuilder rb;
    String id;
     PopupPanel detailPanel=new PopupPanel(true);
     Label l=new Label();
     FlexTable info=new FlexTable();
     int x=200,y=200;



    public void setPositionPopup(int x,int y){
        this.x=x;
        this.y=y;
    }
    public DetailClickHandler(String id) {
        this.id=id;
        rb = new RequestBuilder(RequestBuilder.GET, GWT
                .getHostPageBaseURL() +"rest/ev/"+id);
        detailPanel.setGlassEnabled(true);


    }
    @Override
    public void onClick(ClickEvent clickEvent) {

        rb.setCallback(new RequestCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {
                //Window.confirm(response.getText());
                if(200==response.getStatusCode()) {
                    getDetailPanel(response.getText());

                    setPositionPopup(Window.getClientWidth()/2-100,Window.getClientHeight()/2-100);


                    detailPanel.setPopupPosition(x,y);
                    if(detailPanel.getWidget()!=null) {

                        detailPanel.show();
                    }
                    else{
                        Window.alert("null widget");
                    }

                }
                else{
                    Window.alert(response.getStatusCode() + "ERROR");
                }

            }

            @Override
            public void onError(Request request, Throwable throwable) {
                Window.alert("ERROR"+throwable.getMessage() );
            }
        });
        try{
            rb.send();
            //Window.alert(rb.getHTTPMethod()+", "+rb.getUrl());
        }
        catch (Exception e){}
    }

    public RequestBuilder getRb() {
        return rb;
    }

    public void setRb(RequestBuilder rb) {
        this.rb = rb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PopupPanel getDetailPanel(String res){

        info.setText(0,0,"Departure");
        info.setText(1,0,"Destination");
        info.setText(2,0,"Place Reste");
        info.setText(3,0,"Place Totale");
        info.setText(4,0,"Participants");


        info.getColumnFormatter().addStyleName(0,"tableEventsRow0");

        //Window.alert("in panel");
        //=======================
        JSONValue jv = JSONParser.parseStrict(res);
        JSONObject jo=jv.isObject();
        String id_e=jo.get("id").toString();
        String villeDepart=GwtMain.eleverGuillemets(jo.get("villeDepart").toString());
        info.setText(0,1,villeDepart);

        String villeDest=GwtMain.eleverGuillemets(jo.get("villeDest").toString());
        info.setText(1,1,villeDest);
        String nbrest=jo.get("nbPlaceReste").toString();
        info.setText(2,1,nbrest);

        String nbtotal=jo.get("voiture").isObject().get("nbPlaceTotal").toString();
        info.setText(3,1,nbtotal);

        JSONValue conducteur=jo.get("conducteur").isObject().get("id");

        JSONArray participants=jo.get("participants").isArray();
        JSONArray commentaires=jo.get("commentaires").isArray();


        for(int i=0;i<participants.size();i++){
            String nom=GwtMain.eleverGuillemets(participants.get(i).isObject().get("nom").toString());
            String parNoms=nom;
            if(conducteur.equals(participants.get(i).isObject().get("id"))){
                parNoms=nom+"(*)";
            }

            if(i==0) {
                info.setText(4 + i, 1, parNoms);
            }
            else{
                info.setText(4 + i, 0, parNoms);
            }
        }
        info.getFlexCellFormatter().setRowSpan(4,0,participants.size());


        //int nbr=info.getRowCount();
        int nbr=4+participants.size();
        info.setText(nbr,0,"Commentaires");
        for(int i=0;i<commentaires.size();i++){
            String commentaire=GwtMain.eleverGuillemets(commentaires.get(i).isObject().get("content").toString());
            if(i==0) {
                info.setText(nbr + i, 1, commentaire);
            }
            else{
                info.setText(nbr + i, 0, commentaire);
            }

        }
        info.getFlexCellFormatter().setRowSpan(nbr,0,commentaires.size());

        //=============================
        String title="Evenement " + id_e + ": (*) est le conducteur.";
        l.setText(title);
        detailPanel.setTitle("Details: "+title);
        GwtMain.setStyleForFlexTable(info);
        //detailPanel.add(l);



        detailPanel.setWidget(info.asWidget());



        return detailPanel;
    }



}

