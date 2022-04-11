package org.jenkinsci.plugins.environmentdashboard;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.model.RunAction2;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.io.IOException;

public class Deployment extends Builder implements SimpleBuildStep {

    private final String env;
    private final String buildNumber;
    private final String Reference;

    @DataBoundConstructor
    public Deployment(String env, String buildNumber, String Reference) {
        this.env = env;
        this.buildNumber = buildNumber;
        this.Reference = Reference;

    }

    public String getEnv() {
        return env;
    }

    public String getBuildNumber() {
        return buildNumber;
    }


    public String getReference() {
        return Reference;
    }
	
    @Override
    public void perform(
            @Nonnull Run<?, ?> run,
            @Nonnull FilePath workspace,
            @Nonnull Launcher launcher,
            @Nonnull TaskListener listener
    ) throws InterruptedException, IOException {
        run.addAction(new DeploymentAction(
                env,
                buildNumber,
				Reference
        ));
    }

    @Extension
    @Symbol("addDeployToDashboard")
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {
        @Override
        @Nonnull
        public String getDisplayName() {
            return "Deployment";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> t) {
            return true;
        }
    }

    public static final class DeploymentAction implements RunAction2 {

        private Run run;
        private String env;
        private String buildNumber;
        private String Reference;

        public DeploymentAction(String env, String buildNumber, String Reference) {
            this.env = env;
            this.buildNumber = buildNumber;
            this.Reference = Reference;
        }

        @Override
        public String getIconFileName() {
            return null;
        }

        @Override
        public String getDisplayName() {
            return String.format(
                    "Deployment %s to %s",
                    buildNumber,
                    Reference,
					env
            );
        }

        @Override
        public String getUrlName() {
            return null;
        }


        public String getBuildNumber() {
            return buildNumber;
        }

        public String getReference() {
            return Reference;
        }
		
        public String getEnv() {
            return env;
        }

        public Run getRun() {
            return run;
        }

        @Override
        public void onLoad(Run<?, ?> r) {
            this.run = r;
        }

        @Override
        public void onAttached(Run<?, ?> r) {
            this.run = r;
        }
    }
}
