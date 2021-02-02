package p1;
import javax.swing.*;
import java.awt.*;
public class PatchedButton extends JButton {

    public PatchedButton(){
        super();
    }

    public PatchedButton(Icon icon){
        super(icon);
    }

    public PatchedButton(String text){
        super(text);
    }

    public PatchedButton(String text, Icon icon){
        super(text, icon);
    }

    @Override
    public Color getForeground() {
        //workaround
        if (!isEnabled()) {
                return  Color.LIGHT_GRAY;
        }
        return super.getForeground();
    }
}