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

    /**
     * 
     * @param convertIfPossible Try to convert all curves to this format.
     * @return
     * @throws java.text.ParseException if was unable to convert or curves not supported by this serializer
     */
    public String getData(boolean convertIfPossible) throws ParseException;
    
    /**
     * 
     * @param data
     * @param throwWarnings if set to true, throw parse exceptions. if false show JOptionpanes
     * @throws java.text.ParseException
     */
    public void setData(String data, boolean throwWarnings) throws ParseException;
    
    public String getName();
    
    public String getFileExtension();
}
