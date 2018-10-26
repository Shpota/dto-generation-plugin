package com.sashashpota.dtogenerator;

import com.intellij.codeInsight.generation.actions.BaseGenerateAction;

public class DTOGenerateAction extends BaseGenerateAction {
    public DTOGenerateAction() {
        super(new DTOGenerationHandler("Select Fields for DTO Generation"));
    }
}
