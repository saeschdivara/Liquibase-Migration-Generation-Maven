package app.gitforge.plugins.maven.liquibase;

import app.gitforge.libraries.liquibase.migration.MigrationGenerator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "liquibase-migrator", defaultPhase = LifecyclePhase.COMPILE)
public class LiquibaseMigratorMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	MavenProject project;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Liqui in the house: " + project.getArtifactId());
		getLog().info("Liqui in the house: " + project.getBasedir());

		LocalDateTime currentDateTime = LocalDateTime.now();
		String formattedDateTime = currentDateTime
			.format(DateTimeFormatter.ISO_DATE_TIME)
			.replace(":", "")
			.replace("-", "")
			.replace("T", "")
			.split("\\.")[0];

		try {
			MigrationGenerator.INSTANCE.dumpMigration(
				project.getBasedir() + "/src/main/resources/db/changelog/changes",
				project.getBasedir() + "/src/main/kotlin/",
				project.getBasedir() + "/src/main/resources/db/changelog/changes/",
				formattedDateTime + "_changelog.yml"
			);
		} catch (Exception e) {
			getLog().warn(e);
		}
	}
}
