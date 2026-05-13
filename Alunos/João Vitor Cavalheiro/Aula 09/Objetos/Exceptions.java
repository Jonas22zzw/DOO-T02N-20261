import javax.swing.JOptionPane;

public class Exceptions extends Exception {

    public Exceptions() {

        //super();

    }

    public Exceptions(String mensagem) {
        JOptionPane.showMessageDialog(null,
                        "",
                        "Ai não",
                        JOptionPane.INFORMATION_MESSAGE);
        //super(mensagem);

    }
}