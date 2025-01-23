package project_cs451;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

    private static final int WIDTH = 1000;  // Width of the scene
    private static final int HEIGHT = 780; // Height of the scene
    private double mouseX, mouseY;         // Variables for mouse position
    private double maxTranslateX, maxTranslateY, minTranslateX, minTranslateY; // Limits for translating the scene
    private MediaPlayer mediaPlayer;       // MediaPlayer for background music
    private boolean isSceneChanged = false; // Flag to check if the scene has been changed
    private Scene previousScene;           // Holds the previous scene to return to it
    private Group previousRoot;            // Holds the root node of the previous scene

    @Override
    public void start(Stage primaryStage) {
        // Load background image for Harry Potter scene
        Image image = new Image(getClass().getResourceAsStream("/project_cs451/img/HPbackground.jpg"));
        ImageView galaxyImage = new ImageView(image);
        galaxyImage.setFitWidth(WIDTH);  // Set the width of the background image
        galaxyImage.setFitHeight(HEIGHT);  // Set the height of the background image
        galaxyImage.setPreserveRatio(true);  // Preserve the aspect ratio of the image

        Group root = new Group(galaxyImage);  // Group to hold all scene elements

        // Apply scale transformation to the root group for zoom effect
        Scale scale = new Scale(1, 1, WIDTH / 2.0, HEIGHT / 2.0);
        root.getTransforms().add(scale);

        // Apply translate transformation for panning effect
        Translate translate = new Translate(0, 0);
        root.getTransforms().add(translate);

        // Calculate translation limits based on the image size and scale
        calculateTranslationLimits(image, scale);

        // Create and add 3D objects to the scene (Harry Potter objects)
        Group threeDGroup = createHarryPotter3DObjects();
        root.getChildren().add(threeDGroup);

        // Add background music to the scene
        addBackgroundMusic("LV.wav");

        // Create the scene with the root group
        Scene scene = new Scene(root, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);

        // Add scroll zoom functionality
        addScrollZoom(scene, root, scale, translate, image);

        // Add mouse drag functionality to move the scene
        addMouseDrag(scene, translate, image);

        // Key press event to switch between scenes
        scene.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().toString().equals("SPACE")) {
                if (isSceneChanged) {
                    changeToPreviousScene(primaryStage); // Switch back to the previous scene
                } else {
                    changeScene(primaryStage); // Change to Voldemort scene
                }
            }
        });

        // Set up the primary stage with the initial scene
        primaryStage.setTitle("Sometimes we need space");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Ensure media player stops when the application is closed
        primaryStage.setOnCloseRequest(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        });
    }

    // Method to calculate the limits for panning (translation)
    private void calculateTranslationLimits(Image image, Scale scale) {
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        maxTranslateX = (imageWidth * scale.getX() - WIDTH) / 2;
        maxTranslateY = (imageHeight * scale.getY() - HEIGHT) / 2;
        minTranslateX = Math.min(0, -maxTranslateX);
        minTranslateY = Math.min(0, -maxTranslateY);
    }

    // Create 3D objects for the Harry Potter scene (letters, bags, broomstick, mirror)
    private Group createHarryPotter3DObjects() {
        PhongMaterial blackMaterial = new PhongMaterial();
        blackMaterial.setDiffuseColor(Color.BLACK);  // Set color for the material

        // Materials for different 3D objects
        PhongMaterial lettersMaterial = new PhongMaterial();
        lettersMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/project_cs451/img/lettersFront.jpg")));

        PhongMaterial lettersMaterial1 = new PhongMaterial();
        lettersMaterial1.setDiffuseMap(new Image(getClass().getResourceAsStream("/project_cs451/img/lettersBack.jpg")));

        PhongMaterial bagsMaterial = new PhongMaterial();
        bagsMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/project_cs451/img/bags.png")));

        PhongMaterial brooMaterial = new PhongMaterial();
        brooMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/project_cs451/img/firebolt.png")));

       
        PhongMaterial goldMaterial = new PhongMaterial();
        goldMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("img/mirror/GoldOld.jpg")));
        
        PhongMaterial brownMaterial = new PhongMaterial();
        brownMaterial.setDiffuseColor(Color.rgb(68, 56, 57));

        PhongMaterial goldMaterial1 = new PhongMaterial();
        goldMaterial1.setDiffuseMap(new Image(getClass().getResourceAsStream("img/mirror/base.png")));

        PhongMaterial back = new PhongMaterial();
        back.setDiffuseMap(new Image(getClass().getResourceAsStream("img/mirror/HP.png")));

        PhongMaterial backTri = new PhongMaterial();
        backTri.setDiffuseMap(new Image(getClass().getResourceAsStream("img/mirror/Tri.png")));

        PhongMaterial detailsMaterial = new PhongMaterial();
        detailsMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("img/mirror/acc.png")));

        PhongMaterial voldemortMaterial = new PhongMaterial();
        voldemortMaterial.setDiffuseColor(Color.DARKRED);

        Group threeDGroup = new Group();

        // Create animated boxes for Harry Potter scene
        Box letters1 = createAnimatedBox(63, 36, 100, lettersMaterial, 447, 593, -200, 11);
        Box letters2 = createAnimatedBox(63, 36, 100, lettersMaterial1, 526, 569, -200, -16);
        Box letters3 = createAnimatedBox(63, 36, 100, lettersMaterial, 519, 510, -200, 46);
        Box letters4 = createAnimatedBox(63, 36, 100, lettersMaterial1, 430, 538, -200, -5);
        Box letters5 = createAnimatedBox(63, 36, 100, lettersMaterial, 440, 490, -200, 5);
        Box letters6 = createAnimatedBox(63, 36, 100, lettersMaterial1, 520, 620, -200, 5);
        Box letters7 = createAnimatedBox(63, 36, 100, lettersMaterial, 400, 630, -200, 20);

        Shape3D bags = new Box(240, 240, 100);
        bags.setMaterial(bagsMaterial);
        bags.setTranslateX(140);
        bags.setTranslateY(640);
        bags.setTranslateZ(-200);

        Shape3D broomstick = new Box(400, 400, 100);
        broomstick.setMaterial(brooMaterial);
        broomstick.setTranslateX(99);
        broomstick.setTranslateY(590);
        broomstick.setTranslateZ(-200);

        // Mirror
        Shape3D Mirror = new Box(240, 450, 0); // Create the mirror with specific dimensions
        Mirror.setMaterial(back); // Set the material for the mirror
        Mirror.setTranslateX(810); // Set the X position of the mirror
        Mirror.setTranslateY(560); // Set the Y position of the mirror

        // Mirror bases
        Shape3D baseMirrorRight = new Box(130, 410, 0); // Right base of the mirror
        baseMirrorRight.setMaterial(goldMaterial1); // Set the material for the right base
        baseMirrorRight.setTranslateX(705); // Set the X position for the right base
        baseMirrorRight.setTranslateY(590); // Set the Y position for the right base

        Shape3D baseMirrorLeft = new Box(130, 410, 0); // Left base of the mirror
        baseMirrorLeft.setMaterial(goldMaterial1); // Set the material for the left base
        baseMirrorLeft.setTranslateX(910); // Set the X position for the left base
        baseMirrorLeft.setTranslateY(590); // Set the Y position for the left base

        // Decorations for the left base
        Shape3D baseLeft = new Box(100, 90, 0); // Front part of the left base
        //define material for baseLeft
        baseLeft.setMaterial(backTri); 
        baseLeft.setTranslateX(910); // Position the left base front
        baseLeft.setTranslateY(364); 

        Shape3D baseLeftbehind = new Box(40, 20, 0); // Back part of the left base
        baseLeftbehind.setMaterial(brownMaterial); // Set the material for the left back base
        baseLeftbehind.setTranslateX(910); // Position the left back base
        baseLeftbehind.setTranslateY(410); 

        // Decorations for the right base
        Shape3D baseRight = new Box(100, 90, 0); // Front part of the right base
        baseRight.setMaterial(backTri); 
        baseRight.setTranslateX(705); // Position the right base front
        baseRight.setTranslateY(364); 

        Shape3D baseRightbehind = new Box(40, 20, 0); // Back part of the right base
        baseRightbehind.setMaterial(brownMaterial); // Set the material for the right back base
        baseRightbehind.setTranslateX(705); // Position the right back base
        baseRightbehind.setTranslateY(410); 

        // Details in the center below the mirror
        Shape3D details = new Box(150, 150, 0); // Box to represent intricate details
        details.setMaterial(detailsMaterial); // Set the material for the details
        details.setTranslateX(810); // Centered beneath the mirror
        details.setTranslateY(370); 

        // Base for positioning
        Shape3D base = new Box(70, 15, 0); // Base shape to position other components
        base.setMaterial(brownMaterial); // Set the material for the base
        base.setTranslateX(60); 
        base.setTranslateY(-10); 

        // Static triangle using TriangleMesh
        float[] points = { // Define the vertices of the triangle
            100, 0, 0,  // Left vertex of the triangle
            0, -100, 0,   // Top vertex of the triangle (flipped downward)
            -100, 0, 0    // Right vertex of the triangle
        };

        float[] texCoords = { // Define texture coordinates
            0.5f, 0.5f,  // Center of the texture
            0.0f, 1.0f,  // Bottom-left corner of the texture
            1.0f, 1.0f   // Bottom-right corner of the texture
        };

        int[] faces = { // Define the triangular face
            0, 0, 1, 1, 2, 2  // Triangle using the points and texture coordinates
        };

        TriangleMesh mesh = new TriangleMesh(); // Create a triangle mesh
        mesh.getPoints().addAll(points); // Add points to the mesh
        mesh.getTexCoords().addAll(texCoords); // Add texture coordinates
        mesh.getFaces().addAll(faces); // Add faces to the mesh

        MeshView triangleMeshView = new MeshView(); // Create a view for the triangle
        triangleMeshView.setMesh(mesh); // Set the triangle mesh
        triangleMeshView.setMaterial(goldMaterial); // Apply material to the triangle
        triangleMeshView.setTranslateX(810); // Align the triangle horizontally with the mirror
        triangleMeshView.setTranslateY(420); // Position the triangle above the mirror
        triangleMeshView.setTranslateZ(0); // Ensure the triangle is at the same depth

        // Decorative spheres on the bases
        Sphere circle1 = new Sphere(10); // Sphere for decoration
        circle1.setMaterial(goldMaterial); // Set the material for the sphere
        circle1.setTranslateX(693); // Position the sphere on the right base
        circle1.setTranslateY(415); 

        Sphere circle2 = new Sphere(10); // Second decorative sphere
        circle2.setMaterial(goldMaterial); // Set the material for the sphere
        circle2.setTranslateX(715); // Position the sphere near the right base
        circle2.setTranslateY(415); 

        Sphere circle3 = new Sphere(10); // Third decorative sphere
        circle3.setMaterial(goldMaterial); // Set the material for the sphere
        circle3.setTranslateX(923); // Position the sphere on the left base
        circle3.setTranslateY(415); 

        Sphere circle4 = new Sphere(10); // Fourth decorative sphere
        circle4.setMaterial(goldMaterial); // Set the material for the sphere
        circle4.setTranslateX(900); // Position the sphere near the left base
        circle4.setTranslateY(415); 

        // Add point light to scene
        PointLight pointLight = new PointLight(Color.BLACK); // Light to enhance the scene's effect
        pointLight.setTranslateX(800); // Position the light at the mirror's location
        pointLight.setTranslateY(500); // Align the light vertically with the mirror
        pointLight.setTranslateZ(-100); // Place the light slightly in front of the mirror

        // Add all 3D objects to the group
        threeDGroup.getChildren().addAll(letters1, letters2, letters3, letters4, letters5, letters6, letters7, bags, broomstick, 
            base, baseLeftbehind, baseRightbehind, triangleMeshView, Mirror, baseMirrorRight, baseMirrorLeft, circle1, circle2, circle3, circle4, 
            baseLeft, baseRight, details, pointLight);

        // Add ambient light to the scene
        addAmbientLight(threeDGroup); // Method to add ambient light to the entire group

        return threeDGroup; // Return the 3D group
    }


    // Method to create animated box (3D object) with a specific rotation and movement
    private Box createAnimatedBox(double width, double height, double depth, PhongMaterial material, double x, double y, double z, double rotation) {
        Box box = new Box(width, height, depth);
        box.setMaterial(material);
        box.setTranslateX(x);
        box.setTranslateY(y);
        box.setTranslateZ(z);
        box.setRotate(rotation);

        TranslateTransition animation = new TranslateTransition(Duration.seconds(2), box);
        animation.setByY(10);  // Move the box by 10 units on the Y-axis
        animation.setCycleCount(TranslateTransition.INDEFINITE);  // Repeat animation indefinitely
        animation.setAutoReverse(true);  // Reverse the animation after each cycle
        animation.play();

        return box;
    }

    // Method to add scroll zoom functionality to the scene
    private void addScrollZoom(Scene scene, Group root, Scale scale, Translate translate, Image image) {
        scene.setOnScroll((ScrollEvent event) -> {
            double delta = event.getDeltaY() > 0 ? 1.1 : 0.9;  // Zoom in or out based on scroll direction
            double newScaleX = scale.getX() * delta;
            double newScaleY = scale.getY() * delta;

            // Ensure zoom level stays within range
            if (newScaleX >= 1.0 && newScaleX <= 3.0) {
                scale.setX(newScaleX);
                scale.setY(newScaleY);
            }

            calculateTranslationLimits(image, scale);  // Recalculate translation limits based on new scale

            // Adjust translation for smooth zooming
            double mouseSceneX = event.getSceneX();
            double mouseSceneY = event.getSceneY();
            double zoomFactor = newScaleX / scale.getX();

            translate.setX(translate.getX() - (mouseSceneX - translate.getX()) * (zoomFactor - 1));
            translate.setY(translate.getY() - (mouseSceneY - translate.getY()) * (zoomFactor - 1));

            constrainTranslation(translate);  // Ensure translation stays within limits
        });
    }

    // Method to add mouse dragging functionality to move the scene
    private void addMouseDrag(Scene scene, Translate translate, Image image) {
        scene.setOnMousePressed((MouseEvent event) -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        scene.setOnMouseDragged((MouseEvent event) -> {
            double offsetX = event.getSceneX() - mouseX;
            double offsetY = event.getSceneY() - mouseY;

            translate.setX(translate.getX() + offsetX);
            translate.setY(translate.getY() + offsetY);

            constrainTranslation(translate);  // Ensure translation stays within limits

            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });
    }

    // Constrain the translation to stay within the limits
    private void constrainTranslation(Translate translate) {
        if (translate.getX() > maxTranslateX) {
            translate.setX(maxTranslateX);
        } else if (translate.getX() < minTranslateX) {
            translate.setX(minTranslateX);
        }

        if (translate.getY() > maxTranslateY) {
            translate.setY(maxTranslateY);
        } else if (translate.getY() < minTranslateY) {
            translate.setY(minTranslateY);
        }
    }

    // Method to add background music to the scene
    private void addBackgroundMusic(String musicFileName) {
        try {
            String musicPath = getClass().getResource("/project_cs451/sound/" + musicFileName).toExternalForm();
            mediaPlayer = new MediaPlayer(new Media(musicPath));
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
            mediaPlayer.setVolume(0.5);  // Set volume to 50%
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    // Method to change the scene to Voldemort's scene
private void changeScene(Stage primaryStage) {
    previousRoot = (Group) primaryStage.getScene().getRoot();
    previousScene = primaryStage.getScene();

    // Load background image for Voldemort's scene
    Image newImage = new Image(getClass().getResourceAsStream("/project_cs451/img/BGVL.jpg"));
    ImageView newBackground = new ImageView(newImage);
    newBackground.setFitWidth(WIDTH);
    newBackground.setFitHeight(HEIGHT);
    newBackground.setPreserveRatio(true);

    Group newRoot = new Group(newBackground);

    // Create 3D objects for Voldemort's scene
    Group new3DGroup = createVoldemort3DObjects();
    newRoot.getChildren().add(new3DGroup);

    // Set up Scale and Translate for Voldemort's scene
    Scale voldemortScale = new Scale(1, 1, WIDTH / 2.0, HEIGHT / 2.0);
    Translate voldemortTranslate = new Translate(0, 0);
    newRoot.getTransforms().addAll(voldemortScale, voldemortTranslate);

    // Change background music
    addBackgroundMusic("HP.wav");

    // Create the new scene
    Scene newScene = new Scene(newRoot, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);

    // Add zoom and drag functionality for Voldemort's scene
    addScrollZoom(newScene, newRoot, voldemortScale, voldemortTranslate, newImage);
    addMouseDrag(newScene, voldemortTranslate, newImage);

    // Add space key event to switch back to the previous scene
    newScene.setOnKeyPressed((KeyEvent event) -> {
        if (event.getCode().toString().equals("SPACE")) {
            changeToPreviousScene(primaryStage); // Return to the previous scene
        }
    });

    // Apply the new scene
    primaryStage.setScene(newScene);

    isSceneChanged = true;
}

// Method to switch back to the previous scene
private void changeToPreviousScene(Stage primaryStage) {
    if (previousScene != null) {
        mediaPlayer.stop();  // Stop the current music
        primaryStage.setScene(previousScene);  // Switch to the previous scene
        isSceneChanged = false;
    }
}

// Method to create 3D objects for Voldemort's scene (mirror and lights)
private Group createVoldemort3DObjects() {
    // Define materials for the 3D objects
    PhongMaterial goldMaterial = new PhongMaterial();
    goldMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("img/mirror/GoldOld.jpg")));
    
    PhongMaterial brownMaterial = new PhongMaterial();
    brownMaterial.setDiffuseColor(Color.rgb(68, 56, 57));

    PhongMaterial goldMaterial1 = new PhongMaterial();
    goldMaterial1.setDiffuseMap(new Image(getClass().getResourceAsStream("img/mirror/base.png")));

    PhongMaterial back = new PhongMaterial();
    back.setDiffuseMap(new Image(getClass().getResourceAsStream("img/mirror/bbb.png")));

    PhongMaterial backTri = new PhongMaterial();
    backTri.setDiffuseMap(new Image(getClass().getResourceAsStream("img/mirror/Tri.png")));

    PhongMaterial detailsMaterial = new PhongMaterial();
    detailsMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("img/mirror/acc.png")));

    PhongMaterial voldemortMaterial = new PhongMaterial();
    voldemortMaterial.setDiffuseColor(Color.DARKRED);

    

    Group voldemort3DGroup = new Group();

    // Create the mirror
    Shape3D Mirror = new Box(250, 450, 0);
    Mirror.setMaterial(back);
    Mirror.setTranslateX(800);
    Mirror.setTranslateY(540);
    
    // Create the base components
    Shape3D baseMirrorRight = new Box(130, 410, 0);
    baseMirrorRight.setMaterial(goldMaterial1);
    baseMirrorRight.setTranslateX(705);
    baseMirrorRight.setTranslateY(590);

    Shape3D baseMirrorLeft = new Box(130, 410, 0);
    baseMirrorLeft.setMaterial(goldMaterial1);
    baseMirrorLeft.setTranslateX(910);
    baseMirrorLeft.setTranslateY(590);

    Shape3D baseLeft = new Box(100, 90, 0);
    baseLeft.setMaterial(backTri);
    baseLeft.setTranslateX(910);
    baseLeft.setTranslateY(364);

    Shape3D baseLeftBehind = new Box(40, 20, 0);
    baseLeftBehind.setMaterial(brownMaterial);
    baseLeftBehind.setTranslateX(910);
    baseLeftBehind.setTranslateY(410);

    Shape3D baseRight = new Box(100, 90, 0);
    baseRight.setMaterial(backTri);
    baseRight.setTranslateX(705);
    baseRight.setTranslateY(364);

    Shape3D baseRightBehind = new Box(40, 20, 0);
    baseRightBehind.setMaterial(brownMaterial);
    baseRightBehind.setTranslateX(705);
    baseRightBehind.setTranslateY(410);

    Shape3D details = new Box(150, 150, 0);
    details.setMaterial(detailsMaterial);
    details.setTranslateX(810);
    details.setTranslateY(370);

    Shape3D base = new Box(70, 15, 0);
    base.setMaterial(brownMaterial);
    base.setTranslateX(60);
    base.setTranslateY(-10);
   
    // Create a static triangle using TriangleMesh
    float[] points = {
        100, 0, 0,  // Left point of the triangle
        0, -100, 0, // Top point of the triangle (flipped to appear below)
        -100, 0, 0  // Right point of the triangle
    };

    float[] texCoords = {
        0.5f, 0.5f,
        0.0f, 1.0f,
        1.0f, 1.0f
    };

    int[] faces = {
        0, 0, 1, 1, 2, 2  // Triangle faces
    };

    TriangleMesh mesh = new TriangleMesh();
    mesh.getPoints().addAll(points);
    mesh.getTexCoords().addAll(texCoords);
    mesh.getFaces().addAll(faces);

    MeshView triangleMeshView = new MeshView();
    triangleMeshView.setMesh(mesh);
    triangleMeshView.setMaterial(goldMaterial);  // Set material for the triangle
    triangleMeshView.setTranslateX(810);
    triangleMeshView.setTranslateY(420);
    triangleMeshView.setTranslateZ(0);

    // Create decorative spheres
    Sphere circle1 = new Sphere(10);
    circle1.setMaterial(goldMaterial);
    circle1.setTranslateX(693);
    circle1.setTranslateY(415);

    Sphere circle2 = new Sphere(10);
    circle2.setMaterial(goldMaterial);
    circle2.setTranslateX(715);
    circle2.setTranslateY(415);

    Sphere circle3 = new Sphere(10);
    circle3.setMaterial(goldMaterial);
    circle3.setTranslateX(923);
    circle3.setTranslateY(415);

    Sphere circle4 = new Sphere(10);
    circle4.setMaterial(goldMaterial);
    circle4.setTranslateX(900);
    circle4.setTranslateY(415);

    // Add all components to the 3D group
    voldemort3DGroup.getChildren().addAll(base, baseLeftBehind, baseRightBehind, triangleMeshView, Mirror, 
        baseMirrorRight, baseMirrorLeft, circle1, circle2, circle3, circle4, baseLeft, baseRight, details);

    addAmbientLight1(voldemort3DGroup);

    return voldemort3DGroup;
}

// Method to add ambient light to a scene
private void addAmbientLight(Group root) {
    AmbientLight ambientLight = new AmbientLight(Color.rgb(190, 190, 190, 1));
    root.getChildren().add(ambientLight);
}

// Method to add ambient light to a scene (alternative)
private void addAmbientLight1(Group root) {
    AmbientLight ambientLight1 = new AmbientLight(Color.DARKGRAY);
    root.getChildren().add(ambientLight1);
}

public static void main(String[] args) {
    System.out.println("Instructions: "
        + "\n- Use the mouse scroll or trackpad to zoom in/out."
        + "\n- Drag with the mouse to pan the scene."
        + "\n- Press SPACE to switch between Harry Potter and Voldemort scenes.");
    launch(args);
 }
}