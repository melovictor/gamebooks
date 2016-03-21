package hu.zagor.gamebooks.initiator.collector;

import hu.zagor.gamebooks.initiator.AbstractGenerator;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CreateCollectorProject extends AbstractGenerator {

    public void create(final CollectorData data) {
        try {
            createProjectStructure(data);
            updateSeriesCollector(data.getBookSeriesFullName());
        } catch (final IOException exception) {
            System.out.println("Failed to create all necessary files.");
            exception.printStackTrace(System.out);
        }
    }

    private void updateSeriesCollector(final String collectionName) throws IOException {
        final File pom = new File("c:\\springsource\\eclipsegit\\collector\\series-collector\\pom.xml");
        final Scanner pomScanner = new Scanner(pom);
        pomScanner.useDelimiter("/z");
        String fileContent = pomScanner.next();
        pomScanner.close();
        fileContent = fileContent.replace("</dependencies>",
            "  <dependency>\r\n" + "      <groupId>hu.zagor.gamebooks.books</groupId>\r\n" + "      <artifactId>language-" + collectionName + "</artifactId>\r\n"
                + "      <version>1.0.0</version>\r\n" + "    </dependency>\r\n" + "    <dependency>\r\n" + "      <groupId>hu.zagor.gamebooks.books</groupId>\r\n"
                + "      <artifactId>media-" + collectionName + "</artifactId>\r\n" + "      <version>1.0.0</version>\r\n" + "    </dependency>\r\n"
                + "    <dependency>\r\n" + "      <groupId>hu.zagor.gamebooks.books</groupId>\r\n" + "      <artifactId>presentation-" + collectionName
                + "</artifactId>\r\n" + "      <version>1.0.0</version>\r\n" + "    </dependency>\r\n" + "  </dependencies>");
        createFile(pom, "", fileContent);
    }

    private void createProjectStructure(final CollectorData data) throws IOException {
        final File baseDir = new File("c:/springsource/eclipsegit/books/" + data.getBookDirName());
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }

        createLanguageProjectStructure(baseDir, data);
        createMediaProjectStructure(baseDir, data);
        createPresentationProjectStructure(baseDir, data);
    }

    private void createMediaProjectStructure(final File baseDir, final CollectorData data) throws IOException {
        final File medDir = new File(baseDir, "media-" + data.getBookSeriesFullName());
        medDir.mkdirs();

        createFile(medDir, ".classpath", getClasspathContent());
        createFile(medDir, ".gitignore", getGitignoreContent());
        createFile(medDir, ".project", getProjectContent("media", data));
        createFile(medDir, "pom.xml", getPomContent("media", data, false));

        createFile(medDir, ".settings", "org.eclipse.wst.common.component", getWstCommonContent("media", data));
        createFile(medDir, ".settings", "org.eclipse.jdt.core.prefs", getJdtCoreContent());
        createFile(medDir, ".settings", "org.eclipse.m2e.core.prefs", getM2eCoreContent());
        createFile(medDir, ".settings", "org.eclipse.wst.validation.prefs", getWstValidationContent());
        createFile(medDir, ".settings", "org.eclipse.wst.common.project.facet.core.xml", getWstCommonCoreContent());

        createDir(medDir, "src/main/resources");
        createFile(medDir, "src/main/resources", "rebel.xml", getRebelContent("media", data));
    }

    private void createLanguageProjectStructure(final File baseDir, final CollectorData data) throws IOException {
        final File langDir = new File(baseDir, "language-" + data.getBookSeriesFullName());
        langDir.mkdirs();

        createFile(langDir, ".classpath", getClasspathContent());
        createFile(langDir, ".gitignore", getGitignoreContent());
        createFile(langDir, ".project", getProjectContent("language", data));
        createFile(langDir, "pom.xml", getPomContent("language", data, false));

        createFile(langDir, ".settings", "org.eclipse.wst.common.component", getWstCommonContent("language", data));
        createFile(langDir, ".settings", "org.eclipse.jdt.core.prefs", getJdtCoreContent());
        createFile(langDir, ".settings", "org.eclipse.m2e.core.prefs", getM2eCoreContent());
        createFile(langDir, ".settings", "org.eclipse.wst.validation.prefs", getWstValidationContent());
        createFile(langDir, ".settings", "org.eclipse.wst.common.project.facet.core.xml", getWstCommonCoreContent());

        createDir(langDir, "src/main/resources");
        createFile(langDir, "src/main/resources", "rebel.xml", getRebelContent("language", data));
    }

    private void createPresentationProjectStructure(final File baseDir, final CollectorData data) throws IOException {
        final File presDir = new File(baseDir, "presentation-" + data.getBookSeriesFullName());
        presDir.mkdirs();

        createFile(presDir, ".checkstyle", getCheckstyleContent());
        createFile(presDir, ".classpath", getClasspathContent());
        createFile(presDir, ".gitignore", getGitignoreContent());
        createFile(presDir, ".project", getPresentationProjectContent("presentation", data));
        createFile(presDir, "pom.xml", getPomContent("presentation", data, true));

        createFile(presDir, ".settings", "org.eclipse.wst.common.component", getWstCommonContent("presentation", data));
        createFile(presDir, ".settings", "org.eclipse.jdt.core.prefs", getJdtCoreContent());
        createFile(presDir, ".settings", "org.eclipse.m2e.core.prefs", getM2eCoreContent());
        createFile(presDir, ".settings", "org.eclipse.wst.validation.prefs", getWstValidationContent());
        createFile(presDir, ".settings", "org.eclipse.wst.common.project.facet.core.xml", getWstCommonCoreContent());

        createDir(presDir, "src/main/java");
        createDir(presDir, "src/main/resources/spring");
        createDir(presDir, "src/test/java");
        createFile(presDir, "src/main/resources", "rebel.xml", getRebelContent("presentation", data));
    }

    private String getRebelContent(final String project, final CollectorData data) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
            + "<application xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.zeroturnaround.com\" xsi:schemaLocation=\"http://www.zeroturnaround.com http://www.zeroturnaround.com/alderaan/rebel-2_0.xsd\">\r\n"
            + "\r\n" + "    <classpath>\r\n" + "        <dir name=\"${rebel.workspace.path}/books/" + data.getBookDirName() + "/" + project + "-"
            + data.getBookSeriesFullName() + "/target/classes\">\r\n" + "        </dir>\r\n" + "    </classpath>\r\n" + "\r\n" + "</application>\r\n";
    }

    private String getGitignoreContent() {
        return "/target/\r\n";
    }

    private String getWstCommonCoreContent() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<faceted-project>\r\n" + "  <installed facet=\"java\" version=\"1.7\"/>\r\n"
            + "  <installed facet=\"jst.utility\" version=\"1.0\"/>\r\n" + "</faceted-project>\r\n";
    }

    private String getWstValidationContent() {
        return "disabled=06target\r\n" + "eclipse.preferences.version=1\r\n";
    }

    private String getM2eCoreContent() {
        return "activeProfiles=\r\n" + "eclipse.preferences.version=1\r\n" + "resolveWorkspaceProjects=true\r\n" + "version=1\r\n";
    }

    private String getJdtCoreContent() {
        return "eclipse.preferences.version=1\r\n" + "org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=enabled\r\n"
            + "org.eclipse.jdt.core.compiler.codegen.targetPlatform=1.7\r\n" + "org.eclipse.jdt.core.compiler.compliance=1.7\r\n"
            + "org.eclipse.jdt.core.compiler.problem.assertIdentifier=error\r\n" + "org.eclipse.jdt.core.compiler.problem.enumIdentifier=error\r\n"
            + "org.eclipse.jdt.core.compiler.problem.forbiddenReference=warning\r\n" + "org.eclipse.jdt.core.compiler.source=1.7\r\n";
    }

    private String getWstCommonContent(final String project, final CollectorData data) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><project-modules id=\"moduleCoreId\" project-version=\"1.5.0\">\r\n" + "    <wb-module deploy-name=\"" + project
            + "-" + data.getBookSeriesFullName() + "\">\r\n" + "        <wb-resource deploy-path=\"/\" source-path=\"/src/main/resources\"/>\r\n"
            + "        <wb-resource deploy-path=\"/\" source-path=\"/src/main/java\"/>\r\n" + "    </wb-module>\r\n" + "</project-modules>\r\n";
    }

    private String getCheckstyleContent() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "\r\n"
            + "<fileset-config file-format-version=\"1.2.0\" simple-config=\"true\" sync-formatter=\"false\">\r\n"
            + "  <fileset name=\"all\" enabled=\"true\" check-config-name=\"gamebooks\" local=\"false\">\r\n"
            + "    <file-match-pattern match-pattern=\".\" include-pattern=\"true\"/>\r\n" + "  </fileset>\r\n"
            + "  <filter name=\"FilesFromPackage\" enabled=\"true\">\r\n" + "    <filter-data value=\"src/main/resources\"/>\r\n" + "  </filter>\r\n"
            + "</fileset-config>\r\n";
    }

    private String getClasspathContent() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<classpath>\r\n"
            + "    <classpathentry kind=\"src\" output=\"target/classes\" path=\"src/main/java\">\r\n" + "        <attributes>\r\n"
            + "            <attribute name=\"optional\" value=\"true\"/>\r\n" + "            <attribute name=\"maven.pomderived\" value=\"true\"/>\r\n"
            + "        </attributes>\r\n" + "    </classpathentry>\r\n"
            + "    <classpathentry excluding=\"**\" kind=\"src\" output=\"target/classes\" path=\"src/main/resources\">\r\n" + "        <attributes>\r\n"
            + "            <attribute name=\"maven.pomderived\" value=\"true\"/>\r\n" + "        </attributes>\r\n" + "    </classpathentry>\r\n"
            + "    <classpathentry kind=\"src\" output=\"target/classes\" path=\"src/test/java\">\r\n" + "        <attributes>\r\n"
            + "            <attribute name=\"optional\" value=\"true\"/>\r\n" + "            <attribute name=\"maven.pomderived\" value=\"true\"/>\r\n"
            + "        </attributes>\r\n" + "    </classpathentry>\r\n" + "    <classpathentry kind=\"con\" path=\"org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER\">\r\n"
            + "        <attributes>\r\n" + "            <attribute name=\"maven.pomderived\" value=\"true\"/>\r\n"
            + "            <attribute name=\"org.eclipse.jst.component.nondependency\" value=\"\"/>\r\n" + "        </attributes>\r\n" + "    </classpathentry>\r\n"
            + "    <classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.7\">\r\n"
            + "        <attributes>\r\n" + "            <attribute name=\"maven.pomderived\" value=\"true\"/>\r\n" + "        </attributes>\r\n"
            + "    </classpathentry>\r\n" + "    <classpathentry kind=\"output\" path=\"target/classes\"/>\r\n" + "</classpath>\r\n";
    }

    private String getProjectContent(final String project, final CollectorData data) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<projectDescription>\r\n" + "    <name>" + project + "-" + data.getBookSeriesFullName() + "</name>\r\n"
            + "    <comment></comment>\r\n" + "    <projects>\r\n" + "    </projects>\r\n" + "    <buildSpec>\r\n" + "        <buildCommand>\r\n"
            + "            <name>org.eclipse.wst.common.project.facet.core.builder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "        <buildCommand>\r\n" + "            <name>org.eclipse.jdt.core.javabuilder</name>\r\n"
            + "            <arguments>\r\n" + "            </arguments>\r\n" + "        </buildCommand>\r\n" + "        <buildCommand>\r\n"
            + "            <name>org.eclipse.m2e.core.maven2Builder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "        <buildCommand>\r\n" + "            <name>org.zeroturnaround.eclipse.rebelXmlBuilder</name>\r\n"
            + "            <arguments>\r\n" + "            </arguments>\r\n" + "        </buildCommand>\r\n" + "        <buildCommand>\r\n"
            + "            <name>org.eclipse.wst.validation.validationbuilder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "    </buildSpec>\r\n" + "    <natures>\r\n" + "        <nature>org.eclipse.jem.workbench.JavaEMFNature</nature>\r\n"
            + "        <nature>org.eclipse.wst.common.modulecore.ModuleCoreNature</nature>\r\n" + "        <nature>org.eclipse.jdt.core.javanature</nature>\r\n"
            + "        <nature>org.eclipse.m2e.core.maven2Nature</nature>\r\n" + "        <nature>org.zeroturnaround.eclipse.jrebelNature</nature>\r\n"
            + "        <nature>org.eclipse.wst.common.project.facet.core.nature</nature>\r\n" + "    </natures>\r\n" + "</projectDescription>\r\n";
    }

    private String getPresentationProjectContent(final String project, final CollectorData data) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<projectDescription>\r\n" + "    <name>" + project + "-" + data.getBookSeriesFullName() + "</name>\r\n"
            + "    <comment></comment>\r\n" + "    <projects>\r\n" + "    </projects>\r\n" + "    <buildSpec>\r\n" + "        <buildCommand>\r\n"
            + "            <name>org.eclipse.wst.common.project.facet.core.builder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "        <buildCommand>\r\n" + "            <name>org.eclipse.jdt.core.javabuilder</name>\r\n"
            + "            <arguments>\r\n" + "            </arguments>\r\n" + "        </buildCommand>\r\n" + "        <buildCommand>\r\n"
            + "            <name>org.eclipse.m2e.core.maven2Builder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "        <buildCommand>\r\n" + "            <name>net.sf.eclipsecs.core.CheckstyleBuilder</name>\r\n"
            + "            <arguments>\r\n" + "            </arguments>\r\n" + "        </buildCommand>\r\n" + "        <buildCommand>\r\n"
            + "            <name>org.zeroturnaround.eclipse.rebelXmlBuilder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "        <buildCommand>\r\n" + "            <name>org.eclipse.wst.validation.validationbuilder</name>\r\n"
            + "            <arguments>\r\n" + "            </arguments>\r\n" + "        </buildCommand>\r\n" + "    </buildSpec>\r\n" + "    <natures>\r\n"
            + "        <nature>org.eclipse.jem.workbench.JavaEMFNature</nature>\r\n" + "        <nature>org.eclipse.wst.common.modulecore.ModuleCoreNature</nature>\r\n"
            + "        <nature>org.eclipse.jdt.core.javanature</nature>\r\n" + "        <nature>org.eclipse.m2e.core.maven2Nature</nature>\r\n"
            + "        <nature>net.sf.eclipsecs.core.CheckstyleNature</nature>\r\n" + "        <nature>org.zeroturnaround.eclipse.jrebelNature</nature>\r\n"
            + "        <nature>org.eclipse.wst.common.project.facet.core.nature</nature>\r\n" + "    </natures>\r\n" + "</projectDescription>\r\n";
    }

    private String getPomContent(final String project, final CollectorData data, final boolean addRulesetDependency) {
        String pomContent = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\r\n"
            + "  <modelVersion>4.0.0</modelVersion>\r\n" + "  <groupId>hu.zagor.gamebooks.books</groupId>\r\n" + "  <artifactId>" + project + "-"
            + data.getBookSeriesFullName() + "</artifactId>\r\n" + "  <version>1.0.0</version>\r\n" + "  <build>\r\n" + "    <plugins>\r\n" + "      <plugin>\r\n"
            + "        <groupId>org.apache.maven.plugins</groupId>\r\n" + "        <artifactId>maven-compiler-plugin</artifactId>\r\n"
            + "        <version>3.0</version>\r\n" + "        <configuration>\r\n" + "          <source>1.7</source>\r\n" + "          <target>1.7</target>\r\n"
            + "        </configuration>\r\n" + "      </plugin>\r\n" + "    </plugins>\r\n" + "  </build>\r\n";
        if (addRulesetDependency) {
            pomContent += "  <dependencies>\r\n" + "    <dependency>\r\n" + "      <groupId>hu.zagor.gamebooks.rulesets</groupId>\r\n" + "      <artifactId>rs-"
                + data.getBasicRuleset() + "</artifactId>\r\n" + "      <version>1.0.0</version>\r\n" + "    </dependency>\r\n" + "  </dependencies>\r\n";
        }
        pomContent += "</project>";
        return pomContent;
    }

}
