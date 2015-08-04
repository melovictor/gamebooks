package hu.zagor.gamebooks.initiator.book;

import hu.zagor.gamebooks.initiator.AbstractGenerator;
import hu.zagor.gamebooks.initiator.Console;

import java.io.File;
import java.io.IOException;

public class GenerateBookMediaProjectFiles extends AbstractGenerator {

    public void generate(final BookBaseData baseData) {
        final File medRootPath = new File("d:/System/eclipsegit/books/" + baseData.getSeriesCode() + "/" + baseData.getTitleCode() + "/" + baseData.getTitleCode()
            + "-med");
        if (!medRootPath.exists()) {
            generateMediaProject(medRootPath, baseData);
        }
    }

    private void generateMediaProject(final File medRootPath, final BookBaseData data) {
        final Console console = Console.getConsole();

        try {

            createFile(medRootPath, "pom.xml", getPomContent(data));
            createFile(medRootPath, ".project", getProjectContent(data));
            createFile(medRootPath, ".classpath", getClasspathContent());
            createFile(medRootPath, ".checkstyle", getCheckstyleContent());

            createFile(medRootPath, ".settings", "org.eclipse.wst.common.component", getComponentContent(data));
            createFile(medRootPath, ".settings", "org.eclipse.jdt.core.prefs", getJdtContent());
            createFile(medRootPath, ".settings", "org.eclipse.m2e.core.prefs", getM2eContent());
            createFile(medRootPath, ".settings", "org.eclipse.wst.common.project.facet.core.xml", getFacetContent());

            createDir(medRootPath, "src/main/resources/" + data.getSeriesCode() + data.getPosition());

            createFile(medRootPath, "src/main/resources/", "rebel.xml", getRebelXmlContent(data));

            if (data.hasInventory()) {
                createDir(medRootPath, "src/main/resources/WEB-INF/tiles/ruleset/" + data.getSeriesCode() + "/" + data.getSeriesCode() + data.getPosition());
                createFile(medRootPath, "src/main/resources/WEB-INF/tiles/ruleset/" + data.getSeriesCode() + "/" + data.getSeriesCode() + data.getPosition(),
                    "charpage.jsp", getCharpageContent());
            }
        } catch (final IOException exception) {
            console.print("Failed to create all necessary files.");
            exception.printStackTrace(System.out);
        }
    }

    private String getCharpageContent() {
        return "<%@page pageEncoding=\"utf-8\" contentType=\"text/html; charset=utf-8\"%>\r\n"
            + "<%@taglib uri=\"http://tiles.apache.org/tags-tiles\" prefix=\"tiles\"%>\r\n" + "\r\n"
            + "<tiles:insertTemplate template=\"../charpage/charpage-basic.jsp\" />\r\n";
    }

    private String getCheckstyleContent() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "\r\n"
            + "<fileset-config file-format-version=\"1.2.0\" simple-config=\"true\" sync-formatter=\"false\">\r\n"
            + "  <fileset name=\"all\" enabled=\"true\" check-config-name=\"hotels.com\" local=\"false\">\r\n"
            + "    <file-match-pattern match-pattern=\".\" include-pattern=\"true\"/>\r\n" + "  </fileset>\r\n"
            + "  <filter name=\"FilesFromPackage\" enabled=\"true\">\r\n" + "    <filter-data value=\"src/main/resources\"/>\r\n" + "  </filter>\r\n"
            + "</fileset-config>";
    }

    private String getRebelXmlContent(final BookBaseData data) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
            + "<application xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.zeroturnaround.com\" xsi:schemaLocation=\"http://www.zeroturnaround.com http://www.zeroturnaround.com/alderaan/rebel-2_0.xsd\">\r\n"
            + "\r\n" + "    <classpath>\r\n" + "        <dir name=\"${rebel.workspace.path}/books/" + data.getSeriesCode() + "/" + data.getTitleCode() + "/"
            + data.getTitleCode() + "-med/target/classes\">\r\n" + "        </dir>\r\n" + "    </classpath>\r\n" + "\r\n" + "</application>";
    }

    private String getComponentContent(final BookBaseData data) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<project-modules id=\"moduleCoreId\" project-version=\"1.5.0\">\r\n" + "    <wb-module deploy-name=\""
            + data.getTitleCode() + "-med\">\r\n" + "        <wb-resource deploy-path=\"/\" source-path=\"/src/main/java\"/>\r\n"
            + "        <wb-resource deploy-path=\"/\" source-path=\"/src/main/resources\"/>\r\n" + "    </wb-module>\r\n" + "</project-modules>\r\n";
    }

    private String getJdtContent() {
        return "eclipse.preferences.version=1\r\n" + "org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=enabled\r\n"
            + "org.eclipse.jdt.core.compiler.codegen.targetPlatform=1.7\r\n" + "org.eclipse.jdt.core.compiler.compliance=1.7\r\n"
            + "org.eclipse.jdt.core.compiler.problem.assertIdentifier=error\r\n" + "org.eclipse.jdt.core.compiler.problem.enumIdentifier=error\r\n"
            + "org.eclipse.jdt.core.compiler.problem.forbiddenReference=warning\r\n" + "org.eclipse.jdt.core.compiler.source=1.7\r\n";
    }

    private String getM2eContent() {
        return "activeProfiles=\r\n" + "eclipse.preferences.version=1\r\n" + "resolveWorkspaceProjects=true\r\n" + "version=1\r\n";
    }

    private String getFacetContent() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<faceted-project>\r\n" + "  <installed facet=\"java\" version=\"1.7\"/>\r\n"
            + "  <installed facet=\"jst.utility\" version=\"1.0\"/>\r\n" + "</faceted-project>\r\n";
    }

    private String getProjectContent(final BookBaseData data) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<projectDescription>\r\n" + "    <name>" + data.getTitleCode() + "-med</name>\r\n"
            + "    <comment></comment>\r\n" + "    <projects>\r\n" + "    </projects>\r\n" + "    <buildSpec>\r\n" + "        <buildCommand>\r\n"
            + "            <name>org.eclipse.wst.common.project.facet.core.builder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "        <buildCommand>\r\n" + "            <name>org.eclipse.jdt.core.javabuilder</name>\r\n"
            + "            <arguments>\r\n" + "            </arguments>\r\n" + "        </buildCommand>\r\n" + "        <buildCommand>\r\n"
            + "            <name>org.eclipse.wst.validation.validationbuilder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "        <buildCommand>\r\n" + "            <name>org.eclipse.m2e.core.maven2Builder</name>\r\n"
            + "            <arguments>\r\n" + "            </arguments>\r\n" + "        </buildCommand>\r\n" + "        <buildCommand>\r\n"
            + "            <name>org.zeroturnaround.eclipse.rebelXmlBuilder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "        <buildCommand>\r\n" + "            <name>net.sf.eclipsecs.core.CheckstyleBuilder</name>\r\n"
            + "            <arguments>\r\n" + "            </arguments>\r\n" + "        </buildCommand>\r\n" + "    </buildSpec>\r\n" + "    <natures>\r\n"
            + "        <nature>org.eclipse.jem.workbench.JavaEMFNature</nature>\r\n" + "        <nature>org.eclipse.wst.common.modulecore.ModuleCoreNature</nature>\r\n"
            + "        <nature>org.eclipse.jdt.core.javanature</nature>\r\n" + "        <nature>org.eclipse.m2e.core.maven2Nature</nature>\r\n"
            + "        <nature>org.eclipse.wst.common.project.facet.core.nature</nature>\r\n" + "        <nature>org.zeroturnaround.eclipse.jrebelNature</nature>\r\n"
            + "        <nature>net.sf.eclipsecs.core.CheckstyleNature</nature>\r\n" + "    </natures>\r\n" + "</projectDescription>\r\n";

    }

    private String getClasspathContent() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
            + "<classpath>\r\n"
            + "    <classpathentry kind=\"src\" output=\"target/classes\" path=\"src/main/java\">\r\n"
            + "        <attributes>\r\n"
            + "            <attribute name=\"optional\" value=\"true\"/>\r\n"
            + "            <attribute name=\"maven.pomderived\" value=\"true\"/>\r\n"
            + "        </attributes>\r\n"
            + "    </classpathentry>\r\n"
            + "    <classpathentry kind=\"src\" output=\"target/classes\" path=\"src/test/java\">\r\n"
            + "        <attributes>\r\n"
            + "            <attribute name=\"optional\" value=\"true\"/>\r\n"
            + "            <attribute name=\"maven.pomderived\" value=\"true\"/>\r\n"
            + "        </attributes>\r\n"
            + "    </classpathentry>\r\n"
            + "    <classpathentry kind=\"src\" path=\"src/main/resources\"/>\r\n"
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

    private String getPomContent(final BookBaseData data) {
        return "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\r\n"
            + "  <modelVersion>4.0.0</modelVersion>\r\n" + "  <groupId>hu.zagor.gamebooks.books."
            + data.getSeriesCode()
            + "."
            + data.getTitleCode()
            + "</groupId>\r\n"
            + "  <artifactId>"
            + data.getTitleCode()
            + "-med</artifactId>\r\n"
            + "  <version>1.0.0</version>\r\n"
            + "  <build>\r\n"
            + "    <plugins>\r\n"
            + "      <plugin>\r\n"
            + "          <groupId>org.apache.maven.plugins</groupId>\r\n"
            + "          <artifactId>maven-compiler-plugin</artifactId>\r\n"
            + "          <version>3.0</version>\r\n"
            + "          <configuration>\r\n"
            + "             <source>1.7</source>\r\n"
            + "             <target>1.7</target>\r\n"
            + "          </configuration>\r\n"
            + "      </plugin>\r\n"
            + "    </plugins>\r\n"
            + "  </build>\r\n"
            + "  <dependencies>\r\n"
            + "    <dependency>\r\n"
            + "      <groupId>hu.zagor.gamebooks.rulesets</groupId>\r\n"
            + "      <artifactId>rs-"
            + data.getRuleset()
            + "</artifactId>\r\n" + "      <version>1.0.0</version>\r\n" + "    </dependency>\r\n" + "  </dependencies>\r\n" + "</project>";
    }

}
