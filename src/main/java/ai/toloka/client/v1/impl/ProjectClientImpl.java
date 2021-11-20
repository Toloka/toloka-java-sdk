/*
 * Copyright 2021 YANDEX LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.toloka.client.v1.impl;

import java.util.Date;

import com.fasterxml.jackson.core.type.TypeReference;

import ai.toloka.client.v1.ModificationResult;
import ai.toloka.client.v1.SearchResult;
import ai.toloka.client.v1.impl.validation.Assertions;
import ai.toloka.client.v1.operation.Operation;
import ai.toloka.client.v1.project.Project;
import ai.toloka.client.v1.project.ProjectArchiveOperation;
import ai.toloka.client.v1.project.ProjectClient;
import ai.toloka.client.v1.project.ProjectSearchRequest;

public class ProjectClientImpl extends AbstractClientImpl implements ProjectClient {

    private static final String PROJECTS_PATH = "projects";
    private static final String ARCHIVE_ACTION_PATH = "archive";

    ProjectClientImpl(TolokaClientFactoryImpl factory) {
        super(factory);
    }

    @Override
    public SearchResult<Project> findProjects(final ProjectSearchRequest request) {
        return find(request, PROJECTS_PATH, new TypeReference<SearchResult<Project>>() {});
    }

    @Override
    public Project getProject(final String projectId) {
        return get(projectId, PROJECTS_PATH, Project.class);
    }

    @Override
    public ModificationResult<Project> createProject(final Project project) {
        Assertions.checkArgNotNull(project, "Project may not be null");

        return create(project, PROJECTS_PATH, Project.class, null);
    }

    @Override
    public ModificationResult<Project> updateProject(final String projectId, final Project project) {
        Assertions.checkArgNotNull(projectId, "Project id may not be null");
        Assertions.checkArgNotNull(project, "Project may not be null");

        return update(projectId, project, PROJECTS_PATH, Project.class);
    }

    @Override
    public ProjectArchiveOperation archiveProject(String projectId) {
        Assertions.checkArgNotNull(projectId, "Id may not be null");

        ProjectArchiveOperation operation =
                (ProjectArchiveOperation) executeAction(projectId, PROJECTS_PATH, ARCHIVE_ACTION_PATH, Operation.class);
        if (operation == null) {
            return ProjectArchiveOperation.createPseudo(new Date());
        }

        return operation;
    }
}
