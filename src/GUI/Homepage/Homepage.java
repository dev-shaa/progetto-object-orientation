package GUI.Homepage;

import Entities.*;
import GUI.Homepage.Categories.*;
import GUI.Homepage.References.*;
import GUI.Homepage.Search.*;
import GUI.Homepage.UserInfo.LogoutListener;
import GUI.Homepage.UserInfo.UserInfoPanel;

import Controller.AuthorController;
import Controller.CategoryController;
import Controller.Controller;
import Controller.ReferenceController;

import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import javax.swing.*;

import GUI.Homepage.References.Editor.*;
import Entities.References.OnlineResources.*;
import Entities.References.OnlineResources.Image;
import Entities.References.PhysicalResources.*;

/**
 * Classe che si occupa di impostare le componenti base della pagina principale, che mostra tutti i riferimenti e le categorie.
 */
public class Homepage extends JFrame implements CategorySelectionListener, LogoutListener, ReferenceSearchListener, ReferenceEditorOptionListener {

    private Controller controller;

    private ReferencePanel referencePanel;
    private CategoriesPanel categoriesPanel;
    private SearchPanel referenceSearchPanel;

    private CategoryController categoryController;
    private ReferenceController referenceController;
    private AuthorController authorController;

    private ArticleEditor articleEditor;
    private BookEditor bookEditor;
    private ThesisEditor thesisEditor;
    private ImageEditor imageEditor;
    private SourceCodeEditor sourceCodeEditor;
    private VideoEditor videoEditor;
    private WebsiteEditor websiteEditor;

    public Homepage(Controller controller, CategoryController categoryController, ReferenceController referenceController, AuthorController authorController, User user) {
        setController(controller);

        this.categoryController = categoryController;
        this.referenceController = referenceController;
        this.authorController = authorController;

        setTitle("Pagina principale");
        setMinimumSize(new Dimension(400, 400));
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, "Sicuro di volere uscire?", "Esci", JOptionPane.YES_NO_OPTION);

                if (confirmDialogBoxOption == JOptionPane.YES_OPTION)
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        setContentPane(contentPane);

        categoriesPanel = new CategoriesPanel(categoryController);
        categoriesPanel.getTreePanel().addSelectionListener(this);

        referencePanel = new ReferencePanel(this, referenceController);
        referencePanel.addListener(this);

        referenceSearchPanel = new SearchPanel(categoryController, authorController);
        referenceSearchPanel.addReferenceSearchListener(this);

        JSplitPane subSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoriesPanel, referencePanel);
        subSplitPane.setResizeWeight(0.15);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, referenceSearchPanel);
        splitPane.setResizeWeight(1);

        UserInfoPanel userInfoPanel = new UserInfoPanel(user);
        userInfoPanel.addLogoutListener(this);

        contentPane.add(userInfoPanel, BorderLayout.NORTH);
        contentPane.add(splitPane, BorderLayout.CENTER);
    }

    public void setController(Controller controller) throws IllegalArgumentException {
        if (controller == null)
            throw new IllegalArgumentException("controller non può essere null");

        this.controller = controller;
    }

    // public void setUser(User user) {
    // if (user == null)
    // throw new IllegalArgumentException("user non può essere null");

    // this.user = user;
    // }

    @Override
    public void onLogout() {
        controller.openLoginPage();
    }

    @Override
    public void onCategorySelected(Category selectedCategory) {
        referencePanel.setReferences(referenceController.getReferences(selectedCategory));
    }

    @Override
    public void onReferenceSearch(Search search) {
        referencePanel.setReferences(referenceController.getReferences(search));
    }

    @Override
    public void onArticleEditorCall(Article article) {
        if (articleEditor == null)
            articleEditor = new ArticleEditor(categoryController, referenceController, authorController);

        articleEditor.setVisible(true, article);
    }

    @Override
    public void onBookEditorCall(Book book) {
        if (bookEditor == null)
            bookEditor = new BookEditor(categoryController, referenceController, authorController);

        bookEditor.setVisible(true, book);
    }

    @Override
    public void onThesisEditorCall(Thesis thesis) {
        if (thesisEditor == null)
            thesisEditor = new ThesisEditor(categoryController, referenceController, authorController);

        thesisEditor.setVisible(true, thesis);
    }

    @Override
    public void onSourceCodeEditorCall(SourceCode sourceCode) {
        if (sourceCodeEditor == null)
            sourceCodeEditor = new SourceCodeEditor(categoryController, referenceController, authorController);

        sourceCodeEditor.setVisible(true, sourceCode);
    }

    @Override
    public void onImageEditorCall(Image image) {
        if (imageEditor == null)
            imageEditor = new ImageEditor(categoryController, referenceController, authorController);

        imageEditor.setVisible(true, image);
    }

    @Override
    public void onVideoEditorCall(Video video) {
        if (videoEditor == null)
            videoEditor = new VideoEditor(categoryController, referenceController, authorController);

        videoEditor.setVisible(true, video);
    }

    @Override
    public void onWebsiteEditorCall(Website website) {
        if (websiteEditor == null)
            websiteEditor = new WebsiteEditor(categoryController, referenceController, authorController);

        websiteEditor.setVisible(true, website);
    }

}
