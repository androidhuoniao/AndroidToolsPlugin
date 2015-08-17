package org.sssta.androidtools.generator;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.util.XmlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.sssta.androidtools.util.LayoutUtils;

/**
 * Created by cauchywei on 15/8/17.
 */
public abstract class BaseFvbiGeneratorAction extends BaseGenerateAction {

    public BaseFvbiGeneratorAction() {
        super(null);
    }

    public BaseFvbiGeneratorAction(CodeInsightActionHandler handler) {
        super(handler);
    }


    @Override
    protected boolean isValidForFile(Project project, Editor editor, PsiFile file) {

        int offset = editor.getCaretModel().getOffset();
        PsiElement psiElement = file.findElementAt(offset);

        if(!PlatformPatterns.psiElement().inside(PsiMethodCallExpression.class).accepts(psiElement)) {
            return false;
        }

        PsiMethodCallExpression psiMethodCallExpression = PsiTreeUtil.getParentOfType(psiElement, PsiMethodCallExpression.class);
        if(psiMethodCallExpression == null) {
            return false;
        }

        PsiMethod psiMethod = psiMethodCallExpression.resolveMethod();
        if(psiMethod == null) {
            return false;
        }

        if (psiMethod.getName().equals("setContentView") || psiMethod.getName().equals("inflate")){
            return true;
        }

        return super.isValidForFile(project, editor, file);
    }

    @Override
    public void actionPerformedImpl(@NotNull Project project,@NotNull Editor editor) {
        super.actionPerformedImpl(project, editor);


        PsiFile layoutXmlFile = LayoutUtils.findLayoutXmlFile(project, editor);

        layoutXmlFile.accept(new XmlElementVisitor() {
            @Override
            public void visitXmlTag(XmlTag tag) {
                super.visitXmlTag(tag);

                XmlAttribute idAttribute = tag.getAttribute("android:id");

            }
        });

    }

    public abstract void generator(@NotNull Project project,@NotNull Editor editor,PsiFile xmlFile);

}
