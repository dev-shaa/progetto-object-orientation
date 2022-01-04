public class MainWindow {

    private MainWindowFrame mainWindowFrame;
    // private CategoriesManager categoriesManager;

    public MainWindow(Controller controller, User user) {
        mainWindowFrame = new MainWindowFrame(this, controller, user);
    }

    public void setVisible(boolean b) {
        mainWindowFrame.setVisible(b);
    }
}
