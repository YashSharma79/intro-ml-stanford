package com.walk.onyourside.model;

import android.content.Context;
import android.content.res.AssetManager;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableDocument;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

public class DatabaseManager {

    private Database database;

    private Context context;

    public DatabaseManager(Context context, String documentName){
        this.context = context;
        GetDatabase();
        JSONToDocument(documentName);
    }

    void GetDatabase(){

        try {
            com.couchbase.lite.DatabaseConfiguration config = new com.couchbase.lite.DatabaseConfiguration(context);
            database = new Database("walk_db", config);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

    }

    /*Reads JSON from file
      Converts it into a ConceptStructure object
      ConceptStructure object is converted to a map
      Map is saved in the document in database
     */
    void JSONToDocument(String documentName){

//        System.out.println("In JSON Document");
        String jsonInput = ReadJSONFile(documentName + ".txt");
        ObjectMapper mapper = new ObjectMapper();

        ConceptStructure ps = null;

        //JSON to object
        try {
            ps = mapper.readValue(jsonInput, ConceptStructure.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Object to Map
        Map<String,Object> map = mapper.convertValue(ps, new TypeReference<Map<String, Object>>() {});
        MutableDocument m = new MutableDocument(documentName).setFloat("version", 2.0F).setString("type","SDK");

        //Set map in document
        m.setData(map);

        SaveDocument(m);
//        System.out.println( ps.toString() );

    }

    /*
     * DocumentToObject - Converts a Couchbase document to a ConceptStructure object
     * Retrieves the document,converts document to Map,
     * then Map is converted to ConceptStructure object
     *
     * @param: Name of the document to be converted
     * @return: A ConceptStructure object
     * ERRNOS: Null document
     */
    protected ConceptStructure DocumentToObject(String documentName){


        Document doc = database.getDocument(documentName);

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> stringObjectMap = doc.toMap();

        ConceptStructure ps = mapper.convertValue(stringObjectMap, ConceptStructure.class);

        return ps;
    }

    String ReadJSONFile(String fileName){

        AssetManager am = context.getAssets();
        InputStream is = null;
        StringBuilder pseudocodeJSON = new StringBuilder();

        try
        {
            is = am.open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            for(String line; (line = bufferedReader.readLine()) != null;) {
                pseudocodeJSON.append(line);
            }

        }

        catch (IOException e)
        {
            e.printStackTrace();
        }


        return pseudocodeJSON.toString();
    }


    void SaveDocument(MutableDocument m){

        try
        {
            database.save(m);
        }

        catch (CouchbaseLiteException e)
        {
            e.printStackTrace();
        }

    }

    String TraverseMap(Map<String,Object> stringObjectMap){


        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Object> entry : stringObjectMap.entrySet())
        {
            String k = entry.getKey() + "/" + entry.getValue();
            sb.append(k);

        }

        return sb.toString();

    }

}
