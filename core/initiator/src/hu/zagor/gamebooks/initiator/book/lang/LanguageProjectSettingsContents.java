package hu.zagor.gamebooks.initiator.book.lang;

import hu.zagor.gamebooks.initiator.book.BookBaseData;
import hu.zagor.gamebooks.initiator.book.BookLangData;

public class LanguageProjectSettingsContents {

    public static String getFacetContent() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<faceted-project>\r\n" + "  <installed facet=\"java\" version=\"1.7\"/>\r\n"
            + "  <installed facet=\"jst.utility\" version=\"1.0\"/>\r\n" + "</faceted-project>\r\n";
    }

    public static String getM2eContent() {
        return "activeProfiles=\r\n" + "eclipse.preferences.version=1\r\n" + "resolveWorkspaceProjects=true\r\n" + "version=1\r\n";
    }

    public static String getJdtContent() {
        return "eclipse.preferences.version=1\r\n" + "org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=enabled\r\n"
            + "org.eclipse.jdt.core.compiler.codegen.targetPlatform=1.7\r\n" + "org.eclipse.jdt.core.compiler.compliance=1.7\r\n"
            + "org.eclipse.jdt.core.compiler.problem.assertIdentifier=error\r\n" + "org.eclipse.jdt.core.compiler.problem.enumIdentifier=error\r\n"
            + "org.eclipse.jdt.core.compiler.problem.forbiddenReference=warning\r\n" + "org.eclipse.jdt.core.compiler.source=1.7\r\n";
    }

    public static String getValidationPreferences() {
        return "disabled=06target\r\n" + "eclipse.preferences.version=1\r\n";
    }

    public static String getComponentContent(final BookLangData data) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<project-modules id=\"moduleCoreId\" project-version=\"1.5.0\">\r\n" + "    <wb-module deploy-name=\""
            + data.getSeriesCode() + data.getPosition() + "\">\r\n" + "        <wb-resource deploy-path=\"/\" source-path=\"/src/main/java\"/>\r\n"
            + "        <wb-resource deploy-path=\"/\" source-path=\"/src/main/resources\"/>\r\n" + "    </wb-module>\r\n" + "</project-modules>";
    }

    public static String getClasspathContent() {
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
            + "        <attributes>\r\n" + "            <attribute name=\"maven.pomderived\" value=\"true\"/>\r\n" + "        </attributes>\r\n"
            + "    </classpathentry>\r\n" + "    <classpathentry kind=\"output\" path=\"target/classes\"/>\r\n" + "</classpath>\r\n";
    }

    public static String getCheckstyleContent() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "\r\n"
            + "<fileset-config file-format-version=\"1.2.0\" simple-config=\"true\" sync-formatter=\"false\">\r\n"
            + "  <fileset name=\"all\" enabled=\"true\" check-config-name=\"hotels.com\" local=\"false\">\r\n"
            + "    <file-match-pattern match-pattern=\".\" include-pattern=\"true\"/>\r\n" + "  </fileset>\r\n"
            + "  <filter name=\"FilesFromPackage\" enabled=\"true\">\r\n" + "    <filter-data value=\"src/main/resources\"/>\r\n" + "  </filter>\r\n"
            + "</fileset-config>";
    }

    public static String getProjectContent(final BookLangData data) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<projectDescription>\r\n" + "    <name>" + data.getSeriesCode() + data.getPosition() + "</name>\r\n"
            + "    <comment></comment>\r\n" + "    <projects>\r\n" + "    </projects>\r\n" + "    <buildSpec>\r\n" + "        <buildCommand>\r\n"
            + "            <name>org.eclipse.wst.common.project.facet.core.builder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "        <buildCommand>\r\n" + "            <name>org.eclipse.jdt.core.javabuilder</name>\r\n"
            + "            <arguments>\r\n" + "            </arguments>\r\n" + "        </buildCommand>\r\n" + "        <buildCommand>\r\n"
            + "            <name>org.springframework.ide.eclipse.core.springbuilder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "        <buildCommand>\r\n" + "            <name>org.eclipse.wst.validation.validationbuilder</name>\r\n"
            + "            <arguments>\r\n" + "            </arguments>\r\n" + "        </buildCommand>\r\n" + "        <buildCommand>\r\n"
            + "            <name>org.eclipse.m2e.core.maven2Builder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "        <buildCommand>\r\n" + "            <name>org.zeroturnaround.eclipse.rebelXmlBuilder</name>\r\n"
            + "            <arguments>\r\n" + "            </arguments>\r\n" + "        </buildCommand>\r\n" + "        <buildCommand>\r\n"
            + "            <name>net.sf.eclipsecs.core.CheckstyleBuilder</name>\r\n" + "            <arguments>\r\n" + "            </arguments>\r\n"
            + "        </buildCommand>\r\n" + "    </buildSpec>\r\n" + "    <natures>\r\n" + "        <nature>org.eclipse.jem.workbench.JavaEMFNature</nature>\r\n"
            + "        <nature>org.eclipse.wst.common.modulecore.ModuleCoreNature</nature>\r\n"
            + "        <nature>org.springframework.ide.eclipse.core.springnature</nature>\r\n" + "        <nature>org.eclipse.jdt.core.javanature</nature>\r\n"
            + "        <nature>org.eclipse.m2e.core.maven2Nature</nature>\r\n" + "        <nature>org.eclipse.wst.common.project.facet.core.nature</nature>\r\n"
            + "        <nature>org.zeroturnaround.eclipse.jrebelNature</nature>\r\n" + "        <nature>net.sf.eclipsecs.core.CheckstyleNature</nature>\r\n"
            + "    </natures>\r\n" + "</projectDescription>\r\n";
    }

    public static String getPomContent(final BookBaseData baseData, final BookLangData data) {
        final String beginning = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
            + "  xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\r\n" + "  <modelVersion>4.0.0</modelVersion>\r\n"
            + "  <groupId>hu.zagor.gamebooks.books." + baseData.getSeriesCode() + "." + baseData.getTitleCode() + "</groupId>\r\n" + "  <artifactId>"
            + data.getSeriesCode() + data.getPosition() + "</artifactId>\r\n" + "  <version>1.0.0</version>\r\n" + "  <dependencies>\r\n";
        String mediaOrParent = "";
        if (!baseData.getSeriesCode().equals(data.getSeriesCode()) || baseData.hasMediaProject()) {
            mediaOrParent = "    <dependency>\r\n" + "      <groupId>hu.zagor.gamebooks.books." + baseData.getSeriesCode() + "." + baseData.getTitleCode()
                + "</groupId>\r\n" + "      <artifactId>"
                + (baseData.getSeriesCode().equals(data.getSeriesCode()) ? baseData.getTitleCode() + "-med" : baseData.getSeriesCode() + baseData.getPosition())
                + "</artifactId>\r\n" + "      <version>1.0.0</version>\r\n" + "    </dependency>\r\n";
        }
        final String end = "    <dependency>\r\n" + "      <groupId>hu.zagor.gamebooks</groupId>\r\n" + "      <artifactId>core</artifactId>\r\n"
            + "      <version>1.0.0</version>\r\n" + "    </dependency>\r\n" + "    <dependency>\r\n"
            + "      <groupId>hu.zagor.gamebooks.rulesets</groupId>\r\n" + "      <artifactId>rs-" + baseData.getRuleset() + "</artifactId>\r\n"
            + "      <version>1.0.0</version>\r\n" + "    </dependency>\r\n" + "  </dependencies>\r\n  <build>\r\n" + "    <plugins>\r\n" + "      <plugin>\r\n"
            + "          <groupId>org.apache.maven.plugins</groupId>\r\n" + "          <artifactId>maven-compiler-plugin</artifactId>\r\n"
            + "          <version>3.0</version>\r\n" + "          <configuration>\r\n" + "             <source>1.7</source>\r\n"
            + "             <target>1.7</target>\r\n" + "          </configuration>\r\n" + "      </plugin>\r\n" + "    </plugins>\r\n" + "  </build>\r\n" + "\r\n"
            + "</project>";
        return beginning + mediaOrParent + end;
    }
}
