package com.example.appwebservicexml;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.System.in;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void reception(View view) {
        Asynchrone as = new Asynchrone();
        as.execute("http://10.0.2.2/webservice/chaps.xml");
    }

    public class Asynchrone extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            //On va utiliser une classe Java qui contient toutes les methodes permettant de
            //traiter un fichier XML, cette classe repose sur les evenements.
            //elle permet de differentier les types de tag que l'on rencontre
            // START_DOCUMENT,END_DOCUMENT, START_TAG, END_TAG
            HttpURLConnection httpURLConnection = null;
            String resultat = "";
            //On va obtenir une variable type XmlPullParseFactory
            //ici pour obtenir une instance de la classe on a pas utilisé un constructeur
            //mais on utilise un DESIGN PATTERN qui s'appelle factory
            //il permet d'encapsuler la construction de l'objet
            // pour faciliter l'instantiation
            try {
                XmlPullParserFactory pullParserFactory=XmlPullParserFactory.newInstance();
                //Creer un objet de type XmlPullParser a partir de la factory que l'on obtenu
                //ci-sessus.Cet objet va permettre de traiter le document XML comme on l'a explique
                //dans le premier commentaire
                XmlPullParser xmlPullParser=pullParserFactory.newPullParser();
                //On recupere le fichier xml ser le serveur afin d'obtenir un flux de lecture
                //sur le fichier
                URL url=new URL(strings[0]);
                    httpURLConnection= (HttpURLConnection) url.openConnection();
                    InputStream inputStream=new BufferedInputStream(httpURLConnection.getInputStream());
                    //definir le fichier à recuperer comme entrée pour le parser XML
                    xmlPullParser.setInput(inputStream,null);
                    //recuperer les evenements decrit dans le fichier XML
                int eventType=xmlPullParser.getEventType();
                //on va parcourir les evenements
                while(eventType!= XmlPullParser.END_DOCUMENT){
                    if(eventType==XmlPullParser.START_DOCUMENT) {
                        //On teste si on est au debut du document si oui on ecrit "DEBUT DU DOCUMENT" dans le log.
                        Log.d("RESULTAT", "debut du doc");
                    } else if(eventType==XmlPullParser.START_TAG){
                        //On testera egalment s'il s'agit d'un TAG de debut on va parcourir ses proprietés
                        Log.d("RESULTAT", "Tag: "+ xmlPullParser.getName());
                        //On recupere le nombre d'attributs
                        int nombreAtt=xmlPullParser.getAttributeCount();
                        //On boucle sur les attributs pour les afficher
                        for(int i=0;i<nombreAtt;i++){
                            Log.d("RESULTAT", "valeur attribut: " +xmlPullParser.getAttributeValue(i));
                        }
                    } else if(eventType==XmlPullParser.END_TAG){
                        //En fin, si c'est le TAG de fin  on va ecrire le nom du TAG.
                            Log.d("RESULTAT", "nom attribut: "+ xmlPullParser.getName());
                    }
                    //on passe à l'evenement suivant
                    eventType=xmlPullParser.next();
                }//Fin boucle while
                in.close();
                httpURLConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
            }
            return null;
        }
    }

}