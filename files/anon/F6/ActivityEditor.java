/*
 * ActivityEditor.java
 *
 * FUTURE6: ACT & ACT PROPS EDITOR, (F6ActEdit)
 *
 * Copyright (C) 2018, Norbert Batfai. PhD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Ez a program szabad szoftver; terjesztheto illetve modosithato a
 * Free Software Foundation altal kiadott GNU General Public License
 * dokumentumaban leirtak; akar a licenc 3-as, akar (tetszoleges) kesobbi
 * valtozata szerint.
 *
 * Ez a program abban a remenyben kerul kozreadasra, hogy hasznos lesz,
 * de minden egyeb GARANCIA NELKUL, az ELADHATOSAGRA vagy VALAMELY CELRA
 * VALO ALKALMAZHATOSAGRA valo szarmaztatott garanciat is beleertve.
 * Tovabbi reszleteket a GNU General Public License tartalmaz.
 *
 * A felhasznalonak a programmal egyutt meg kell kapnia a GNU General
 * Public License egy peldanyat; ha megsem kapta meg, akkor
 * tekintse meg a <http://www.gnu.org/licenses/> oldalon.
 *
 * Version history
 *
 * 0.0.1, 14 Jan 18.
 *
 * Basic usage
 *
 * $ javac ActivityEditor.java
 * $ java ActivityEditor --city=Debrecen --props=me.props,gaming.props,programming.props
 *
 * References
 *
 * https://docs.oracle.com/javafx/2/api/javafx/scene/control/TreeItem.html
 * https://docs.oracle.com/javafx/2/ui_controls/tree-view.htm
 *
 * https://www.twitch.tv/videos/218289099
 * https://shrek.unideb.hu/~nbatfai/FUTURE6-series1bevezetes.pdf
 */
class FileTree extends javafx.scene.control.TreeView<java.io.File> {

    class FileTreeItem extends javafx.scene.control.TreeItem<java.io.File> {

        protected boolean leaf = true;

        public FileTreeItem(java.io.File file, javafx.scene.Node icon) {

            super(file, icon);

            leaf = file.isFile();

        }

        @Override
        public boolean isLeaf() {
            return leaf;
        }

        protected boolean treeChanged = true;

        @Override
        public javafx.collections.ObservableList<javafx.scene.control.TreeItem<java.io.File>> getChildren() {

            if (treeChanged) {
                treeChanged = false;
                super.getChildren().setAll(buildChildren(this));
            }
            return super.getChildren();
        }

        private javafx.collections.ObservableList<javafx.scene.control.TreeItem<java.io.File>>
                buildChildren(javafx.scene.control.TreeItem<java.io.File> treeItem) {

            java.io.File file = treeItem.getValue();
            if (file != null) {
                if (file.isDirectory()) {
                    java.io.File[] list = file.listFiles();
                    if (list != null) {
                        javafx.collections.ObservableList<javafx.scene.control.TreeItem<java.io.File>> children = javafx.collections.FXCollections.observableArrayList();

                        for (java.io.File f : list) {
                            if (f.isDirectory()) {
                                children.add(new FileTreeItem(f, new javafx.scene.image.ImageView(actIcon)));
                            } else {
                                children.add(new FileTreeItem(f, new javafx.scene.image.ImageView(actpropsIcon)));
                            }

                        }

                        return children;
                    }
                }
            }

            return javafx.collections.FXCollections.emptyObservableList();
        }

    }

    protected final javafx.scene.image.Image actIcon = new javafx.scene.image.Image(getClass().getResourceAsStream("activity.png"));

    protected final javafx.scene.image.Image actpropsIcon = new javafx.scene.image.Image(getClass().getResourceAsStream("activityprops.png"));

    public void save(javafx.scene.control.TextArea propsEdit, javafx.scene.control.Label actPropsLabel) {

        String old = actPropsLabel.getText();

        try {
            java.io.File oldf = new java.io.File(old);
            if (oldf.exists()) {

                java.io.FileWriter fileWriter = new java.io.FileWriter(oldf);
                fileWriter.write(propsEdit.getText());
                fileWriter.close();

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public FileTree(String cityName, boolean expanded, javafx.scene.control.TextArea propsEdit, javafx.scene.control.Label actPropsLabel) {
        super();
        javafx.scene.control.TreeItem<java.io.File> root = new FileTreeItem(new java.io.File(cityName), new javafx.scene.image.ImageView(actIcon));
        root.setExpanded(expanded);
        setRoot(root);

        setCellFactory((javafx.scene.control.TreeView<java.io.File> p) -> new TextFieldTreeCell(propsEdit));

        setOnMouseClicked((javafx.scene.input.MouseEvent evt) -> {

            if (evt.getClickCount() == 1) {

                javafx.scene.control.TreeItem<java.io.File> item = getSelectionModel().getSelectedItem();

                if (item != null) {

                    java.io.File f = item.getValue();

                    try {
                        java.util.Scanner scanner = new java.util.Scanner(f);
                        StringBuilder fileContents = new StringBuilder();
                        while (scanner.hasNextLine()) {
                            fileContents.append(scanner.nextLine());
                            fileContents.append(System.getProperty("line.separator"));

                        }
                        scanner.close();

                        save(propsEdit, actPropsLabel);

                        actPropsLabel.setText(f.getPath());
                        propsEdit.setText(fileContents.toString());

                    } catch (java.io.FileNotFoundException fnfE) {

                        save(propsEdit, actPropsLabel);

                        propsEdit.setText("");
                        actPropsLabel.setText("");

                    }

                } else {

                    save(propsEdit, actPropsLabel);

                    propsEdit.setText("");
                    actPropsLabel.setText("");

                }
            }
        });

    }

    private final class TextFieldTreeCell extends javafx.scene.control.TreeCell<java.io.File> {

        private final javafx.scene.control.TextField textField = new javafx.scene.control.TextField();
        private final javafx.scene.control.ContextMenu addMenu = new javafx.scene.control.ContextMenu();
        javafx.scene.control.TextArea propsEdit;

        public TextFieldTreeCell(javafx.scene.control.TextArea propsEdit) {
            this.propsEdit = propsEdit;
            javafx.scene.control.MenuItem subaMenuItem = new javafx.scene.control.MenuItem("Uj altevekenyseg");//"New subactivity");
            addMenu.getItems().add(subaMenuItem);
            subaMenuItem.setOnAction((javafx.event.ActionEvent evt) -> {
                java.io.File file = getTreeItem().getValue();

                java.io.File f = new java.io.File(file.getPath() + System.getProperty("file.separator") + "Uj altevekenyseg");

                if (f.mkdir()) {
                    javafx.scene.control.TreeItem<java.io.File> newAct
 //                           = new javafx.scene.control.TreeItem<java.io.File>(f, new javafx.scene.image.ImageView(actIcon));
                           = new FileTreeItem(f, new javafx.scene.image.ImageView(actIcon));
                    getTreeItem().getChildren().add(newAct);
                } else {

                    System.err.println("Cannot create " + f.getPath());

                }
            });

            javafx.scene.control.MenuItem propsMenuItem = new javafx.scene.control.MenuItem("Uj altevekenyseg tulajdonsagok");
            addMenu.getItems().add(propsMenuItem);
            propsMenuItem.setOnAction((javafx.event.ActionEvent evt) -> {
                java.io.File file = getTreeItem().getValue();

                java.io.File f = new java.io.File(file.getPath() + System.getProperty("file.separator") + "Uj altevekenyseg tulajdonsagok");

                try {
                    f.createNewFile();
                } catch (java.io.IOException e) {

                    System.err.println(e.getMessage());

                }

                javafx.scene.control.TreeItem<java.io.File> newProps
//                        = new javafx.scene.control.TreeItem<java.io.File>(f, new javafx.scene.image.ImageView(actpropsIcon));
                        = new FileTreeItem(f, new javafx.scene.image.ImageView(actpropsIcon));
                getTreeItem().getChildren().add(newProps);
            });

        }

        @Override
        public void startEdit() {

            super.startEdit();

            editCell();

            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {

            super.cancelEdit();

            setText((String) getItem().getPath());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(java.io.File item, boolean empty) {

            super.updateItem(item, empty);

            if (empty || item == null) {

                setText(null);
                setGraphic(null);

            } else {

                setText(getItem().toString());
                setGraphic(getTreeItem().getGraphic());

                if (!getTreeItem().isLeaf()) {
                    setContextMenu(addMenu);
                }

            }
        }

        private void editCell() {

            if (getItem() == null) {
                return;
            }

            String oldText = getItem().toString();
            textField.setText(oldText);

            textField.setOnKeyReleased((javafx.scene.input.KeyEvent t) -> {
                if (t.getCode() == javafx.scene.input.KeyCode.ENTER) {

                    String newText = textField.getText();

                    java.io.File newf = new java.io.File(newText);
                    java.io.File oldf = new java.io.File(oldText);
                    try {
                        if (oldf.isDirectory()) {
                            newf.mkdir();
                        } else {
                            newf.createNewFile();
                        }
                    } catch (java.io.IOException e) {

                        System.err.println(e.getMessage());

                    }

                    commitEdit(newf);
                }

            });

        }

    }

}

class StringTree extends javafx.scene.control.TreeView<String> {

    protected final javafx.scene.image.Image propIcon
            = new javafx.scene.image.Image(getClass().getResourceAsStream("props.png"));

    protected final javafx.scene.image.Image actIcon = new javafx.scene.image.Image(getClass().getResourceAsStream("activity.png"));

    protected final javafx.scene.image.Image actpropsIcon = new javafx.scene.image.Image(getClass().getResourceAsStream("activityprops.png"));

    javafx.scene.control.TextArea propsEdit;

    public StringTree(java.util.List<String> properties, boolean expanded, javafx.scene.control.TextArea propsEdit) {

        super();

        this.propsEdit = propsEdit;

        javafx.scene.control.TreeItem<String> root = new javafx.scene.control.TreeItem<String>("En, magam", new javafx.scene.image.ImageView(propIcon));
        root.setExpanded(expanded);
        setRoot(root);

        for (String prop : properties) {

            javafx.scene.control.TreeItem<String> where = root;

            java.util.StringTokenizer st = new java.util.StringTokenizer(prop, "/");
            StringBuilder prevTokens = new StringBuilder();

            while (st.hasMoreTokens()) {
                String t = st.nextToken();

                if (prevTokens.toString().length() > 0) {
                    prevTokens.append(System.getProperty("file.separator"));
                }

                prevTokens.append(t);

                t = prevTokens.toString();

                boolean found = false;
                for (javafx.scene.control.TreeItem<String> actNode : where.getChildren()) {
                    if (actNode.getValue().contentEquals(t)) {
                        found = true;
                        where = actNode;
                        break;
                    }
                }
                if (!found) {
                    javafx.scene.control.TreeItem<String> actNode = new javafx.scene.control.TreeItem<String>(t, new javafx.scene.image.ImageView(propIcon));
                    where.getChildren().add(actNode);
                    where = actNode;

                }

            }

        }

        setOnMouseClicked((javafx.scene.input.MouseEvent mouseEvent) -> {
            {
                if (mouseEvent.getClickCount() == 2) {
                    javafx.scene.control.TreeItem<String> item = getSelectionModel().getSelectedItem();
                    if (item != null) {
                        propsEdit.appendText(item.getValue());
                        propsEdit.appendText(System.getProperty("line.separator"));
                    }
                }
            }
        });

    }

}

public class ActivityEditor extends javafx.application.Application {

    public static void main(String[] args) {
        javafx.application.Application.launch(args);
    }

    @Override
    public void start(javafx.stage.Stage stage) {

        java.util.Map<String, String> cmdParams = this.getParameters().getNamed();

        stage.setTitle("FUTURE 6 - TEV. es TEV. TUL. SZERKESZTO/ACT & ACT PROPS EDITOR");
        javafx.scene.layout.VBox box = new javafx.scene.layout.VBox();
        final javafx.scene.Scene scene = new javafx.scene.Scene(box);
        scene.getStylesheets().add("nord.css");

        String city = "City/";
        if (cmdParams.containsKey("city")) {
            city = city + cmdParams.get("city");
        }

        String props = "me.props,gaming.props,programming.props";
        if (cmdParams.containsKey("props")) {

            props = cmdParams.get("props");

        }
        java.util.List<String> propsList = java.util.Arrays.asList(props.split(","));

        java.util.List<String> properties = new java.util.ArrayList<>();

        for (String p : propsList) {

            java.io.File f = new java.io.File(p);

            try {
                java.util.Scanner scanner = new java.util.Scanner(f);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (!line.startsWith("//")) {
                        properties.add(line);
                    }

                }
                scanner.close();

            } catch (java.io.FileNotFoundException fnfE) {

                fnfE.printStackTrace();

            }

        }

        javafx.scene.control.TextArea propsEdit = new javafx.scene.control.TextArea();

        javafx.scene.control.TreeView<String> stringTree = new StringTree(properties, true, propsEdit);
        stringTree.setEditable(false);

        javafx.scene.control.Label actPropsLabel = new javafx.scene.control.Label("A tevekenysegekhez hozzarendelt tulajdonsagok");

        javafx.scene.control.TreeView<java.io.File> fileTree = new FileTree(city, true, propsEdit, actPropsLabel);
        fileTree.setEditable(true);

        box.getChildren().add(new javafx.scene.control.Label("Tulajdonsagok faja"));

        box.getChildren().add(stringTree);
        box.getChildren().add(new javafx.scene.control.Label("Tevekenysegek faja es a tevekenysegekhez hozzarendelt tulajdonsagok"));
        box.getChildren().add(fileTree);

        box.getChildren().add(actPropsLabel);
        box.getChildren().add(propsEdit);
        box.getChildren().add(new javafx.scene.control.Label("FUTURE6: ACT & ACT PROPS EDITOR, (F6ActEdit) v.: 0.0.1, Szerzoi jog (C) 2018, GNU GPL v3, Batfai Norbert, nbatfai@inf.unideb.hu, nbatfai@gmail.com"));
        stage.setScene(scene);

        javafx.geometry.Rectangle2D primaryScreenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() / 5);
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth((primaryScreenBounds.getWidth() * 3) / 5);
        stage.setHeight(primaryScreenBounds.getHeight());

        stage.show();
    }

}
