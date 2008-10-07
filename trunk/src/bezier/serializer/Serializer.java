/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bezier.serializer;

import java.text.ParseException;

/**
 *
 * @author Gussoh
 */
public interface Serializer {

    public String getData();
    
    public void setData(String data, boolean supressWarnings) throws ParseException;
    
    public String getName();
}
