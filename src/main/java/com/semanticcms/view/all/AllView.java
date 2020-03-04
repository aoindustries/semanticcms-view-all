/*
 * semanticcms-view-all - SemanticCMS view of all content of the current page and all child pages.
 * Copyright (C) 2016, 2017, 2020  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of semanticcms-view-all.
 *
 * semanticcms-view-all is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * semanticcms-view-all is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with semanticcms-view-all.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.semanticcms.view.all;

import com.aoindustries.html.Html;
import com.aoindustries.servlet.http.Dispatcher;
import com.aoindustries.web.resources.registry.Style;
import com.aoindustries.web.resources.servlet.RegistryEE;
import com.semanticcms.core.controller.PageUtils;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.renderer.html.HtmlRenderer;
import com.semanticcms.core.renderer.html.View;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.SkipPageException;

/**
 * The view of all content of the current page and all child pages.
 *
 * TODO: Authorship and copyright in view-all, or author specified per article, or author in article when doesn't match overall page?
 *
 * TODO: Any use of description/keywords per article below?
 */
public class AllView extends View {

	public static final String NAME = "all";

	public static final Style SEMANTICCMS_VIEW_ALL_PRINT = Style.builder()
		.uri("/semanticcms-autogit-style/semanticcms-autogit.css")
		.media("print")
		.build();

	private static final String JSPX_TARGET = "/semanticcms-view-all/view.inc.jspx";

	@WebListener("Registers the \"" + NAME + "\" view in RegistryEE and HtmlRenderer.")
	public static class Initializer implements ServletContextListener {
		@Override
		public void contextInitialized(ServletContextEvent event) {
			ServletContext servletContext = event.getServletContext();

			// TODO: Only add this style to the all view
			// Add our CSS file
			RegistryEE.get(servletContext).global.styles.add(SEMANTICCMS_VIEW_ALL_PRINT);

			// Add this view
			HtmlRenderer.getInstance(servletContext).addView(new AllView());
		}
		@Override
		public void contextDestroyed(ServletContextEvent event) {
			// Do nothing
		}
	}

	private AllView() {}

	@Override
	public Group getGroup() {
		return Group.FIXED;
	}

	@Override
	public String getDisplay() {
		return "View All";
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isApplicable(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, Page page) throws ServletException, IOException {
		return PageUtils.hasChild(servletContext, page);
	}

	@Override
	public String getDescription(Page page) {
		return null;
	}

	@Override
	public String getKeywords(Page page) {
		return null;
	}

	/**
	 * No search index "all" view to avoid duplicate content penalties.
	 */
	@Override
	public boolean getAllowRobots(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, Page page) {
		return false;
	}

	@Override
	public void doView(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, Html html, Page page) throws ServletException, IOException, SkipPageException {
		Dispatcher.include(
			servletContext,
			JSPX_TARGET,
			request,
			response,
			Collections.singletonMap("page", page)
		);
	}
}
