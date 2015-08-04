package hu.zagor.gamebooks.initiator.collector;

import hu.zagor.gamebooks.initiator.AbstractGenerator;
import hu.zagor.gamebooks.initiator.Console;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CreateCollectorProject extends AbstractGenerator {

    public void create() {
        final Console console = Console.getConsole();

        console.print("Please set the name of the collection: ");
        final String collectionName = console.readLine();

        final File baseDir = new File("d:/System/eclipsegit/books/collector/" + collectionName);
        baseDir.mkdirs();

        try {

            createFile(baseDir, "pom.xml", getPomContent(collectionName));
            createFile(baseDir, ".project", getProjectContent(collectionName));
            createFile(baseDir, ".checkstyle", getCheckstyleContent());
            createFile(baseDir, ".classpath", getClasspathContent());

            createFile(baseDir, "META-INF", "MANIFEST.MF", getManifestContent());

            createFile(baseDir, ".settings", "org.eclipse.wst.common.component", getComponentContent(collectionName));
            createFile(baseDir, ".settings", "org.eclipse.jdt.core.prefs", getJdtPrefContent());
            createFile(baseDir, ".settings", "org.eclipse.m2e.core.prefs", getM2ePrefContent());
            createFile(baseDir, ".settings", "org.eclipse.wst.validation.prefs", getValidationContent());
            createFile(baseDir, ".settings", "org.eclipse.wst.common.project.facet.core.xml", getFacetContent());

            updateSeriesCollector(collectionName);

        } catch (final IOException exception) {
            console.print("Failed to create all necessary files.");
            exception.printStackTrace(System.out);
        }
    }

    private void updateSeriesCollector(final String collectionName) throws IOException {
        final File pom = new File("d:\\System\\eclipse\\books\\collector\\series-collector\\pom.xml");
        final Scanner pomScanner = new Scanner(pom);
        pomScanner.useDelimiter("/z");
        String fileContent = pomScanner.next();
        pomScanner.close();
        fileContent = fileContent.replace("</dependencies>", "    <dependency>\r\n" + "      <groupId>hu.zagor.gamebooks.books.collector</groupId>\r\n"
            + "      <artifactId>" + collectionName + "</artifactId>\r\n" + "      <version>1.0.0</version>\r\n" + "    </dependency>\r\n" + "  </dependencies>");
        createFile(pom, "", fileContent);
    }

    private String getManifestContent() {
        return "Manifest-Version: 1.0\r\n" + "Class-Path: \r\n" + "\r\n";
    }

    private String getCheckstyleContent() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "\r\n"
            + "<fileset-config file-format-version=\"1.2.0\" simple-config=\"true\" sync-formatter=\"false\">\r\n"
            + "  <fileset name=\"all\" enabled=\"true\" check-config-name=\"hotels.com\" local=\"false\">\r\n"
            + "    <file-match-pattern match-pattern=\".\" include-pattern=\"true\"/>\r\n" + "  </fileset>\r\n" + "</fileset-config>";
    }

    private String getClasspathContent() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
            + "<classpath>\r\n"
            + "    <classpathentry kind=\"con\" path=\"org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER\">\r\n"
            + "        <attributes>\r\n"
            + "            <attribute name=\"maven.pomderived\" value=\"true\"/>\r\n"
            + "            <attribute name=\"org.eclipse.jst.component.nondependency\" value=\"\"/>\r\n"
            + "        </attributes>\r\n"
            + "    </classpathentry>\r\n"
            + "    <classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.7\">\r\n"
            + "        <attributes>\r\n" + "            <attribute name=\"owner.project.facets\" value=\"java\"/>\r\n" + "        </attributes>\r\n"
            + "    </classpathentry>\r\n" + "    <classpathentry kind=\"output\" path=\"target/classes\"/>\r\n" + "</classpath>\r\n";
    }

    private String getProjectContent(final String collectionName) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<projectDescription>\r\n" + "    <name>" + collectionName + "</name>\r\n"
            + "    <comment></comment>\r\n" + "    <projects>\r\n" + "    </projects>\r\n" + "    <buildSpec>\r\n" + "        <buildCommand>\r\n"
            + "            <name>org.eclipse.wst.common.project.facet.core.builder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "        <buildCommand>\r\n" + "            <name>org.eclipse.jdt.core.javabuilder</name>\r\n"
            + "            <arguments>\r\n" + "            </arguments>\r\n" + "        </buildCommand>\r\n" + "        <buildCommand>\r\n"
            + "            <name>org.eclipse.wst.validation.validationbuilder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "        <buildCommand>\r\n" + "            <name>org.eclipse.m2e.core.maven2Builder</name>\r\n"
            + "            <arguments>\r\n" + "            </arguments>\r\n" + "        </buildCommand>\r\n" + "    </buildSpec>\r\n" + "    <natures>\r\n"
            + "        <nature>org.eclipse.jem.workbench.JavaEMFNature</nature>\r\n" + "        <nature>org.eclipse.wst.common.modulecore.ModuleCoreNature</nature>\r\n"
            + "        <nature>org.eclipse.jdt.core.javanature</nature>\r\n" + "        <nature>org.eclipse.m2e.core.maven2Nature</nature>\r\n"
            + "        <nature>org.eclipse.wst.common.project.facet.core.nature</nature>\r\n" + "    </natures>\r\n" + "</projectDescription>\r\n";
    }

    private String getPomContent(final String collectionName) {
        return "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
            + "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\r\n"
            + "    <modelVersion>4.0.0</modelVersion>\r\n" + "    <groupId>hu.zagor.gamebooks.books.collector</groupId>\r\n" + "    <artifactId>" + collectionName
            + "</artifactId>\r\n" + "    <version>1.0.0</version>\r\n" + "  <build>\r\n" + "    <plugins>\r\n" + "      <plugin>\r\n"
            + "          <groupId>org.apache.maven.plugins</groupId>\r\n" + "          <artifactId>maven-compiler-plugin</artifactId>\r\n"
            + "          <version>3.0</version>\r\n" + "          <configuration>\r\n" + "             <source>1.7</source>\r\n"
            + "             <target>1.7</target>\r\n" + "          </configuration>\r\n" + "      </plugin>\r\n" + "    </plugins>\r\n" + "  </build>\r\n"
            + "    <dependencies>\r\n" + "\r\n" + "    </dependencies>\r\n" + "</project>";
    }

    private String getFacetContent() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<faceted-project>\r\n" + "  <installed facet=\"jst.utility\" version=\"1.0\"/>\r\n"
            + "  <installed facet=\"java\" version=\"1.7\"/>\r\n" + "</faceted-project>\r\n";
    }

    private String getValidationContent() {
        return "disabled=06target\r\n" + "eclipse.preferences.version=1\r\n";
    }

    private String getM2ePrefContent() {
        return "activeProfiles=\r\n" + "eclipse.preferences.version=1\r\n" + "resolveWorkspaceProjects=true\r\n" + "version=1\r\n";
    }

    private String getJdtPrefContent() {
        return "eclipse.preferences.version=1\r\n" + "org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=enabled\r\n"
            + "org.eclipse.jdt.core.compiler.codegen.targetPlatform=1.7\r\n" + "org.eclipse.jdt.core.compiler.compliance=1.7\r\n"
            + "org.eclipse.jdt.core.compiler.problem.assertIdentifier=error\r\n" + "org.eclipse.jdt.core.compiler.problem.enumIdentifier=error\r\n"
            + "org.eclipse.jdt.core.compiler.problem.forbiddenReference=warning\r\n" + "org.eclipse.jdt.core.compiler.source=1.7\r\n";
    }

    private String getComponentContent(final String collectionName) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><project-modules id=\"moduleCoreId\" project-version=\"1.5.0\">\r\n" + "    <wb-module deploy-name=\""
            + collectionName + "\">\r\n" + "    </wb-module>\r\n" + "</project-modules>\r\n";
    }

}
