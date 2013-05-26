/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package populatedatabase;

import java.util.Iterator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.tooling.GlobalGraphOperations;
import static populatedatabase.PopulateDatabase.graphDb;

/**
 *
 * @author Sebastien
 */
public class Populate {

    private GraphDatabaseService database;
    private String indexName;
    
    /**
     * Objet qui remplit une base de donnée de 10000 nodes avec des propriétés
     * String aléatoire
     * @param database une instance de GraphDatabaseService existante
     * @param indexName le nom de l'index qui sera crée, ce nom correspond au nom
     * de la propriéte des nodes i.e. Node key value; indexName = key
     */
    public Populate() {
    }

    public void setDatabase(GraphDatabaseService database) {
        this.database = database;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }
    
    public void populateByValue() {
        for (int id = 0; id < 1000; id++) {
            createAndIndexNode(generate(25 + (int) Math.round(Math.random() * 225)), indexName);
        }
        for (int id = 1000; id < 2000; id++) {
            createAndIndexNode(generate(25 + (int) Math.round(Math.random() * 225)), indexName);
        }
        for (int id = 2000; id < 3000; id++) {
            createAndIndexNode(generate(25 + (int) Math.round(Math.random() * 225)), indexName);
        }
        for (int id = 3000; id < 4000; id++) {
            createAndIndexNode(generate(25 + (int) Math.round(Math.random() * 225)), indexName);
        }
        for (int id = 4000; id < 5000; id++) {
            createAndIndexNode(generate(25 + (int) Math.round(Math.random() * 225)), indexName);
        }
        for (int id = 5000; id < 6000; id++) {
            createAndIndexNode(generate(25 + (int) Math.round(Math.random() * 225)), indexName);
        }
        for (int id = 6000; id < 7000; id++) {
            createAndIndexNode(generate(25 + (int) Math.round(Math.random() * 225)), indexName);
        }
        for (int id = 7000; id < 8000; id++) {
            createAndIndexNode(generate(25 + (int) Math.round(Math.random() * 225)), indexName);
        }
        for (int id = 8000; id < 9000; id++) {
            createAndIndexNode(generate(25 + (int) Math.round(Math.random() * 225)), indexName);
        }
        for (int id = 9000; id < 10000; id++) {
            createAndIndexNode(generate(25 + (int) Math.round(Math.random() * 225)), indexName);
        }
    }
    
    // peuple 
    public void populateProperties(String propertyName){
        Iterable<Node> iter = GlobalGraphOperations.at(database).getAllNodes();
        long id = 0;
        
        System.out.println(IteratorUtil.count(iter.iterator()));
        Iterator<Node> it = iter.iterator();
        while (it.hasNext()) {
            Node node = it.next();
            addAndSetProperty(id, propertyName, node);
            id++;
            if ( (id % 500) == 0) {
                System.out.println(id + "/10000 \t");
            }
        }
    }
   
    private void addAndSetProperty(long id, String propertyName, Node node){
        Transaction tx = database.beginTx();
        try{
            node.setProperty(propertyName, String.valueOf(id));
            database.index().forNodes(propertyName).add(node, propertyName, node.getProperty(propertyName));
            tx.success();
        } catch (Exception e){
            tx.failure();
            System.err.println("addAndSetProperty erreur: " + e.getMessage());
        }
        finally {
            tx.finish();
        }
    }
    
    // Creer des valeurs et index leur nom avec IndexService
    private void createAndIndexNode(final String value, final String propertyName) {
        Transaction tx = database.beginTx();
        try {
            Node node = database.createNode();
            node.setProperty(propertyName, value);
            database.index().forNodes(propertyName).add(node, propertyName, node.getProperty(propertyName));
            tx.success();
        } catch (Exception e) {
            tx.failure();
            System.err.println("createAndIndexNode erreur");
        }
        finally {
            tx.finish();
        }
    }

    // Retourne un String aléatoire avec une longueur prise en paramètres
    private String generate(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder pass = new StringBuilder(length);
        for (int x = 0; x < length; x++) {
            int i = (int) (Math.random() * chars.length());
            pass.append(chars.charAt(i));
        }
        return pass.toString();
    }
}
