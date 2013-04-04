/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.ostia.dashboard.view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;

import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;

import com.ostia.dashboard.DashboardUI;
import com.ostia.dashboard.data.DataProvider;
import com.ostia.dashboard.data.DataProvider.Movie;
import com.vaadin.addon.timeline.Timeline;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ApisView extends VerticalLayout implements View {

    private Timeline timeline;

    Color[] colors = new Color[] { new Color(52, 154, 255),
            new Color(242, 81, 57), new Color(255, 201, 35),
            new Color(83, 220, 164) };
    int colorIndex = -1;

	private AceEditor editor;

    @Override
    public void enter(ViewChangeEvent event) {
    	//setSizeFull();
        addStyleName("timeline");
     

        Label header = new Label("Make your Api");
        header.addStyleName("h1");
        addComponent(header);

        HorizontalLayout toolbar = new HorizontalLayout();
        
        toolbar.setSpacing(true);
        toolbar.setMargin(true);
        toolbar.addStyleName("toolbar");
        addComponent(toolbar);

        
        final ComboBox tipoApi = new ComboBox("Method");
        tipoApi.addItem("POST");
        tipoApi.addItem("GET");
        tipoApi.addItem("PUT");
        
        tipoApi.setWidth("200px");
        toolbar.addComponent(tipoApi);
        
        final TextField url = new TextField("http://ostia.com/api/demoapp/*");
        url.setWidth("200px");
        url.setInputPrompt("service1");

        Button add = new Button("Add");
        add.addStyleName("default");
        add.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                
            }
        });
        
        toolbar.addComponent(url);
        
        final ComboBox comboTemplates = new ComboBox("Templates");
        comboTemplates.addItem("Get all");
        comboTemplates.addItem("Find one");
        comboTemplates.addItem("Put one");
        comboTemplates.setWidth("200px");
        
        
        final ComboBox comboModel = new ComboBox("Model");
        comboModel.addItem("Person");
        comboModel.addItem("Customer");
        comboModel.addItem("House");
        comboModel.setWidth("200px");
        
        HorizontalLayout toolbarTM = new HorizontalLayout();
        toolbarTM.setSpacing(true);
        toolbarTM.setMargin(true);
        toolbarTM.addStyleName("toolbar");
        addComponent(toolbarTM);
        
        toolbarTM.addComponent(comboTemplates);
        toolbarTM.addComponent(comboModel);
        
        addComponent(toolbarTM);
        
        editor = new AceEditor();
        
        final ComboBox themeSelect = new ComboBox("Theme");
		
		for (AceTheme theme : AceTheme.values()) {
			themeSelect.addItem(theme);
		}
		themeSelect.setNullSelectionAllowed(false);
		themeSelect.setImmediate(true);
		themeSelect.addValueChangeListener(new ValueChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				editor.setTheme((AceTheme)themeSelect.getValue());
			}
		});
		themeSelect.select(AceTheme.github);
		themeSelect.setWidth("200px");
		
		final ComboBox modeSelect = new ComboBox("Mode");
		for (AceMode mode : AceMode.values()) {
			modeSelect.addItem(mode);
		}
		modeSelect.setNullSelectionAllowed(false);
		modeSelect.setImmediate(true);
		modeSelect.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				editor.setMode((AceMode)modeSelect.getValue());
			}
		});
		modeSelect.select(AceMode.javascript);
		modeSelect.setWidth("200px");
		
		HorizontalLayout toolbarSettings = new HorizontalLayout();
		toolbarSettings.setSpacing(true);
		toolbarSettings.setMargin(true);
		toolbarSettings.addStyleName("toolbar");
        
		addComponent(toolbarSettings);
		
        toolbarTM.addComponent(themeSelect);
        toolbarTM.addComponent(modeSelect);
        
        HorizontalLayout toolbarEditor = new HorizontalLayout();
        toolbarEditor.setSpacing(true);
        toolbarEditor.setMargin(true);
		
        toolbarEditor.addComponent(editor);
        toolbarEditor.addComponent(add);
        editor.setWidth("600px");
        editor.setHeight("300px");
        
        addComponent(toolbarEditor);
        
        
        
    }

    private void addSelectedMovie(final ComboBox movieSelect) {
        if (movieSelect.getValue() != null
                && !movieSelect.getValue().equals("")) {
            String title = movieSelect.getValue().toString();
            addDataSet(title);
            movieSelect.removeItem(title);
            movieSelect.setValue(null);
        }
    }

    private void addDataSet(String title) { 
        IndexedContainer revenue = ((DashboardUI) getUI()).dataProvider
                .getRevenueForTitle(title);
        timeline.addGraphDataSource(revenue, "timestamp", "revenue");
        colorIndex = (colorIndex >= colors.length - 1 ? 0 : ++colorIndex);
        timeline.setGraphOutlineColor(revenue, colors[colorIndex]);
        timeline.setBrowserOutlineColor(revenue, colors[colorIndex]);
        timeline.setBrowserFillColor(revenue, colors[colorIndex].brighter());
        timeline.setGraphLegend(revenue, title);
        timeline.setEventCaptionPropertyId("date");
        timeline.setVerticalAxisLegendUnit(revenue, "$");
    }
}
