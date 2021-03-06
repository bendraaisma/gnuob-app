package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.StringValidator;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.generic.converter.ByteArrayConverter;
import com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;
import wicket.contrib.tinymce4.TinyMceBehavior;
import wicket.contrib.tinymce4.ajax.TinyMceAjaxSubmitModifier;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class CategoryContentViewOrEditPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class CategoryContentEditFragment extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class ContentEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class SaveAjaxButton extends BootstrapAjaxButton {

        private static final long serialVersionUID = 2695394292963384938L;

        public SaveAjaxButton(final String id, final IModel<String> model, final Form<Content> form, final Buttons.Type type) {
          super(id, model, form, type);
          setSize(Buttons.Size.Small);
          add(new LoadingBehavior(Model.of(CategoryContentViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY))), new TinyMceAjaxSubmitModifier());
        }

        @Override
        protected void onError(final AjaxRequestTarget target, final Form<?> form) {
          form.add(new TooltipValidation());
          target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(CategoryContentViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)))));
        }

        @Override
        protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
          if (((Content) form.getDefaultModelObject()).getId() == 0) {
            ((Category) CategoryContentViewOrEditPanel.this.getDefaultModelObject()).getContents().add((Content) form.getDefaultModelObject());
          }
          target.add(form.setOutputMarkupId(true));
          target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(CategoryContentViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)))));
          target.add(CategoryContentViewOrEditPanel.this.getParent().setOutputMarkupId(true));
        }
      }

      private static final String CONTENT_ID = "content";

      private static final String FORMAT_ID = "format";

      private static final String NAME_ID = "name";

      private static final long serialVersionUID = 675532301330293648L;

      private final BootstrapForm<Content> contentEditForm;

      private final SaveAjaxButton saveAjaxButton;

      public ContentEditTable(final String id, final IModel<Category> model) {
        super(id, model);
        contentEditForm = new BootstrapForm<Content>("contentEditForm", new CompoundPropertyModel<Content>(CategoryContentViewOrEditPanel.this.selectedModel));
        saveAjaxButton = new SaveAjaxButton("save", Model.of(CategoryContentViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)), contentEditForm,
            Buttons.Type.Primary);
      }

      @Override
      protected void onInitialize() {
        contentEditForm.add(new RequiredTextField<String>(NAME_ID).add(StringValidator.maximumLength(128)).setOutputMarkupId(true));
        contentEditForm.add(new TextField<String>(FORMAT_ID).setRequired(true).add(StringValidator.maximumLength(128)).setOutputMarkupId(true));
        contentEditForm.add(new TextArea<byte[]>(CONTENT_ID) {

          private static final long serialVersionUID = -7341359315847579440L;

          @Override
          public <C> IConverter<C> getConverter(final Class<C> type) {
            if (byte[].class.isAssignableFrom(type)) {
              return (IConverter<C>) new ByteArrayConverter();
            } else {
              return super.getConverter(type);
            }
          }
        }.setRequired(true).add(new TinyMceBehavior()).setOutputMarkupId(true));
        contentEditForm.add(saveAjaxButton.setOutputMarkupId(true));
        add(contentEditForm.setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final String CONTENT_EDIT_TABLE_ID = "contentEditTable";

    private static final String CATEGORY_CONTENT_EDIT_FRAGMENT_MARKUP_ID = "categoryContentEditFragment";

    private static final String CATEGORY_CONTENT_VIEW_OR_EDIT_FRAGMENT_ID = "categoryContentViewOrEditFragment";

    private static final long serialVersionUID = 2975560176491444462L;

    private final ContentEditTable contentEditTable;

    public CategoryContentEditFragment() {
      super(CATEGORY_CONTENT_VIEW_OR_EDIT_FRAGMENT_ID, CATEGORY_CONTENT_EDIT_FRAGMENT_MARKUP_ID, CategoryContentViewOrEditPanel.this,
          CategoryContentViewOrEditPanel.this.getDefaultModel());
      contentEditTable = new ContentEditTable(CONTENT_EDIT_TABLE_ID, (IModel<Category>) CategoryContentEditFragment.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(contentEditTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class CategoryContentViewFragment extends Fragment {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class ContentViewTable extends WebMarkupContainer {

      private static final long serialVersionUID = -8067077655681760267L;

      private final BootstrapForm<Content> contentViewForm;

      public ContentViewTable(final String id, final IModel<Category> model) {
        super(id, model);
        contentViewForm = new BootstrapForm<Content>("contentViewForm", new CompoundPropertyModel<Content>(CategoryContentViewOrEditPanel.this.selectedModel));
      }

      @Override
      protected void onInitialize() {
        contentViewForm.add(new RequiredTextField<String>(NAME_ID).setOutputMarkupId(true));
        contentViewForm.add(new TextField<String>(FORMAT_ID).setOutputMarkupId(true));
        contentViewForm.add(new Label(CONTENT_ID) {

          private static final long serialVersionUID = 721587245052671908L;

          @Override
          public <C> IConverter<C> getConverter(final Class<C> type) {
            if (byte[].class.isAssignableFrom(type)) {
              return (IConverter<C>) new ByteArrayConverter();
            } else {
              return super.getConverter(type);
            }
          }
        }.setEscapeModelStrings(false));
        add(contentViewForm.setOutputMarkupId(true));
        add(new TableBehavior());
        super.onInitialize();
      }
    }

    private static final String CONTENT_VIEW_TABLE_ID = "contentViewTable";

    private static final String CATEGORY_CONTENT_VIEW_FRAGMENT_MARKUP_ID = "categoryContentViewFragment";

    private static final String CATEGORY_CONTENT_VIEW_OR_EDIT_FRAGMENT_ID = "categoryContentViewOrEditFragment";

    private static final long serialVersionUID = -8203670073215506846L;

    private static final String CONTENT_ID = "content";

    private static final String FORMAT_ID = "format";

    private static final String NAME_ID = "name";

    private final ContentViewTable contentViewTable;

    public CategoryContentViewFragment() {
      super(CATEGORY_CONTENT_VIEW_OR_EDIT_FRAGMENT_ID, CATEGORY_CONTENT_VIEW_FRAGMENT_MARKUP_ID, CategoryContentViewOrEditPanel.this,
          CategoryContentViewOrEditPanel.this.getDefaultModel());
      contentViewTable = new ContentViewTable(CONTENT_VIEW_TABLE_ID, (IModel<Category>) CategoryContentViewFragment.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(contentViewTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final long serialVersionUID = 7032777283917504797L;

  private IModel<Content> selectedModel;

  public CategoryContentViewOrEditPanel(final String id, final IModel<Category> model) {
    super(id, model);
    selectedModel = Model.of(new Content());
  }

  public void setSelectedModel(final IModel<Content> selectedModel) {
    this.selectedModel = selectedModel;
  }
}
