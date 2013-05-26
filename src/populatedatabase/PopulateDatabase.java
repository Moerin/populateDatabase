/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package populatedatabase;

import java.lang.reflect.Field;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.tooling.GlobalGraphOperations;

/**
 *
 * @author Seb
 */
public class PopulateDatabase {

    static GraphDatabaseService graphDb;
    static Index<Node> nodeIndex;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("path");
        System.out.println("Base de donnée initialisé");

        Populate pop = new Populate();
        pop.setDatabase(graphDb);

        System.out.println(IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllNodes()));

//        Field[] fields = Data.class.getDeclaredFields();
//        for (Field field : fields) {
//            System.out.println(field.getName());
//            pop.setIndexName(field.getName());
//            if ("value".equals(field.getName())) {
//                System.out.println("Value trouvé");
//                for (int index = 0; index < 10; index++) {    
//                    Transaction tx = graphDb.beginTx();
//                    try {
//                        pop.setIndexName(field.getName());
//                        pop.createAndIndexNode(pop.generate(25 + (int) Math.round(Math.random() * 225)), field.getName());
//                        // Signale que la transaction a reussi
//                        tx.success();
//                    } catch (Exception e) { // TODO : identifier les vrai exceptions
//            //            logger.warn("Node : find failed");
//                        System.err.println("Node : find failed");
//                        tx.failure(); // A VERIFIER SI CETTE LIGNE DE CODE EST UTILE
//                    } finally {
//                        // Cloture la transaction
//                        tx.finish();
//                    } 
//                }
//            }
//       
//        }

        Transaction tx = graphDb.beginTx();
        try {
            Field[] fields = Data.class.getDeclaredFields();
            for (Field field : fields) {
                System.out.println(field.getName());
                pop.setIndexName(field.getName());
                if ("id".equals(field.getName())) {
                    System.out.println("Id trouvé");
                    pop.populateProperties(field.getName());
                } else {
                    System.out.println("Value trouvé");
                    pop.populateByValue();
                }
            }
//            // Signale que la transaction a reussi
            tx.success();
        } catch (Exception e) { // TODO : identifier les vrai exceptions
//            logger.warn("Node : find failed");
            System.err.println("Node : find failed");
            tx.failure(); // A VERIFIER SI CETTE LIGNE DE CODE EST UTILE
        } finally {
            // Cloture la transaction
            tx.finish();
        }

//        System.out.println(graphDb.getNodeById(1000).getPropertyKeys().toString());
//        
        System.out.println("value : " + graphDb.getNodeById(1500).getProperty("value"));
        System.out.println("id : " + graphDb.getNodeById(1500).getProperty("id"));

//        tx = graphDb.beginTx();
//        try {
//            graphDb.getNodeById(1500).setProperty("id", "truc2");
//            tx.success();   
//        } catch (Exception e) {
//            tx.failure();
//        } finally {
//            tx.finish();
//        }

//
//        Data data = querySingle("value", String.valueOf(graphDb.getNodeById(1500).getProperty("value")));
//
//        System.out.println(data.getId());
//        
//        graphDb.getNodeById(1500);



        graphDb.shutdown();
        System.out.println("Fermeture de la base de donnée");

    }

    public static Data querySingle(String name, String value) {
        Data data = new Data();

        nodeIndex = graphDb.index().forNodes(name);

        Transaction tx = graphDb.beginTx();

        try {
            IndexHits<Node> hits = nodeIndex.get(name, value);
            Node node = hits.getSingle();
            data.setId(node.getId());
            // Signale que la transaction a reussi
            tx.success();
        } catch (Exception e) {
//            logger.warn("Query : querySingle failed");
            System.err.println("Query : querySingle failed");
            // Signale que la transaction a été un echec
            tx.failure(); // A VERIFIER SI CETTE LIGNE DE CODE EST UTILE
        } finally {
            // Cloture la transaction
            tx.finish();
        }
        return data;
    }
}
