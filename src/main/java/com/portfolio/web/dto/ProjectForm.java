package com.portfolio.web.dto;

import java.util.ArrayList;
import java.util.List;

import com.portfolio.domain.Project;
import com.portfolio.domain.TestAccount;
import com.portfolio.domain.TestEndpoint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProjectForm {

    private Long id;

    @NotBlank(message = "Please enter a title")
    @Size(max = 120)
    private String title;

    @NotBlank(message = "Please enter a summary")
    @Size(max = 500)
    private String summary;

    @NotBlank(message = "Please enter a description")
    private String description;

    private String coverImage;

    private String tags;

    private String liveUrl;

    private String githubUrl;

    private String testNotes;

    private Integer sortOrder = 0;

    private List<TestAccountItem> testAccounts = new ArrayList<>();

    private List<TestEndpointItem> testEndpoints = new ArrayList<>();

    public static ProjectForm from(Project project) {
        ProjectForm form = new ProjectForm();
        form.id = project.getId();
        form.title = project.getTitle();
        form.summary = project.getSummary();
        form.description = project.getDescription();
        form.coverImage = project.getCoverImage();
        form.tags = project.getTags();
        form.liveUrl = project.getLiveUrl();
        form.githubUrl = project.getGithubUrl();
        form.testNotes = project.getTestNotes();
        form.sortOrder = project.getSortOrder();
        form.testAccounts = new ArrayList<>(project.getTestAccounts().stream().map(TestAccountItem::from).toList());
        form.testEndpoints = new ArrayList<>(project.getTestEndpoints().stream().map(TestEndpointItem::from).toList());
        return form;
    }

    public void applyTo(Project project) {
        project.setTitle(title.trim());
        project.setSummary(summary.trim());
        project.setDescription(description.trim());
        project.setTags(blankToNull(tags));
        project.setLiveUrl(blankToNull(liveUrl));
        project.setGithubUrl(blankToNull(githubUrl));
        project.setTestNotes(blankToNull(testNotes));
        project.setSortOrder(sortOrder == null ? 0 : sortOrder);

        List<TestAccount> accounts = new ArrayList<>();
        if (testAccounts != null) {
            for (TestAccountItem item : testAccounts) {
                if (item == null || item.isBlank()) {
                    continue;
                }
                TestAccount account = new TestAccount();
                account.setRoleName(safe(item.getRoleName()));
                account.setUsername(safe(item.getUsername()));
                account.setPassword(safe(item.getPassword()));
                account.setNotes(safe(item.getNotes()));
                accounts.add(account);
            }
        }
        project.replaceTestAccounts(accounts);

        List<TestEndpoint> endpoints = new ArrayList<>();
        if (testEndpoints != null) {
            for (TestEndpointItem item : testEndpoints) {
                if (item == null || item.isBlank()) {
                    continue;
                }
                TestEndpoint endpoint = new TestEndpoint();
                endpoint.setMethod(safe(item.getMethod()).toUpperCase());
                endpoint.setUrl(safe(item.getUrl()));
                endpoint.setDescription(safe(item.getDescription()));
                endpoints.add(endpoint);
            }
        }
        project.replaceTestEndpoints(endpoints);
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private static String safe(String value) {
        return value == null ? "" : value.trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getTestNotes() {
        return testNotes;
    }

    public void setTestNotes(String testNotes) {
        this.testNotes = testNotes;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<TestAccountItem> getTestAccounts() {
        if (testAccounts == null) {
            testAccounts = new ArrayList<>();
        }
        return testAccounts;
    }

    public void setTestAccounts(List<TestAccountItem> testAccounts) {
        this.testAccounts = testAccounts;
    }

    public List<TestEndpointItem> getTestEndpoints() {
        if (testEndpoints == null) {
            testEndpoints = new ArrayList<>();
        }
        return testEndpoints;
    }

    public void setTestEndpoints(List<TestEndpointItem> testEndpoints) {
        this.testEndpoints = testEndpoints;
    }
}
