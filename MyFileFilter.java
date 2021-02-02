package p1;
import java.io.File;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;


/***************************************************/
public class MyFileFilter extends FileFilter
{
    private String extension;
    private String description;
    ////////////////
    public MyFileFilter()
    {
        setExtension(null);
        setDescription(null);
    }
    ////////////////
    public MyFileFilter(final String ext, final String desc)
    {
        setExtension(ext);
        setDescription(desc);
    }
    ////////////////
    public boolean accept(File f)
    {
        final String filename=f.getName();

        if(	f.isDirectory() || 
        extension==null || 
        filename.toUpperCase()
        .endsWith(extension.toUpperCase()))
            return true;
        return false;

    }
    ////////////////
    public String getDescription()
    {
        return description;
    }
    ////////////////
    public void setDescription(String desc)
    {
        if(desc==null)
            description=new String("All Files(*.*)");
        else
            description=new String(desc);
    }
    ////////////////
    public void setExtension(String ext)
    {
        if(ext==null)
        {extension=null;  return;}

        extension=new String(ext).toLowerCase();
        if(!ext.startsWith("."))
            extension="."+extension;
    }
    ////////////////
}
/***************************************************/