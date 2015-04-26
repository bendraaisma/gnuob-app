package com.netbrasoft.gnuob.application.customer;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.api.Customer;

public class CustomerTab extends AbstractTab {

   private static final long serialVersionUID = 4835579949680085443L;

   public CustomerTab(IModel<String> title) {
      super(title);
   }

   @Override
   public WebMarkupContainer getPanel(String panelId) {
      Customer customer = new Customer();
      customer.setActive(true);
      return new CustomerPanel(panelId, new Model<Customer>(customer));
   }
}
