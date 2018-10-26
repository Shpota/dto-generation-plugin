package com.sashashpota.dtogenerator;

import com.intellij.codeInsight.generation.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class DTOGenerationHandler extends GenerateMembersHandlerBase {
    public DTOGenerationHandler(String title) {
        super(title);
    }

    @Override
    protected ClassMember[] getAllOriginalMembers(PsiClass psiClass) {
        return Arrays.stream(psiClass.getFields())
                .map(PsiFieldMember::new)
                .toArray(ClassMember[]::new);
    }

    @Override
    protected GenerationInfo[] generateMemberPrototypes(
            PsiClass psiClass, ClassMember classMember) {
        return new GenerationInfo[0];
    }

    @Override
    protected List<? extends GenerationInfo> generateMemberPrototypes(
            PsiClass psiClass, ClassMember[] members) {
        JavaDirectoryService directoryService = JavaDirectoryService.getInstance();
        PsiDirectory directory = psiClass.getContainingFile()
                .getContainingDirectory();
        PsiClass dtoClass = directoryService.createClass(
                directory, psiClass.getName() + "DTO"
        );

        Project project = psiClass.getManager().getProject();
        PsiElementFactory factory = JavaPsiFacade
                .getInstance(project).getElementFactory();
        return Arrays.stream(members)
                .map(PsiElementClassMember.class::cast)
                .map(PsiElementClassMember::getElement)
                .filter(PsiField.class::isInstance)
                .map(PsiField.class::cast)
                .map(property -> factory.createField(
                        property.getName(), property.getType()
                ))
                .map(dtoClass::add)
                .map(PsiMember.class::cast)
                .map(PsiGenerationInfo::new)
                .collect(toList());
    }
}
