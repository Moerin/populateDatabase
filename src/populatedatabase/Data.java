/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package populatedatabase;

import java.io.Serializable;
import org.vostok.management.annotations.MBean;

/**
 *
 * @author Sebastien
 */
public class Data implements Serializable {
    // Penser aux differentes propriete qui peuvent etre integres
    private String value;
    private long id;

    public Data() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
