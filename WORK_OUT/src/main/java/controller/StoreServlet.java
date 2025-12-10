package controller;

import data.StoreCategoryData;
import dto.NaverProductDTO;
import dto.StoreCategory;
import service.StoreProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

 
@WebServlet("/store")
public class StoreServlet extends HttpServlet {

    private StoreProductService storeProductService;

    @Override
    public void init() throws ServletException {
        super.init();
        storeProductService = StoreProductService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String categoryId = request.getParameter("category");

        // Default action: list products
        if (action == null || action.equals("list")) {
            handleList(request, response, categoryId);
        }
        // Admin action: initialize all categories
        else if (action.equals("initialize")) {
            handleInitialize(request, response);
        }
        // Admin action: refresh specific category
        else if (action.equals("refresh")) {
            handleRefresh(request, response, categoryId);
        }
        else {
            handleList(request, response, categoryId);
        }
    }

    /**
     * Handle list action (default)
     * URL: /store?category=upper_body
     * URL: /store (shows all categories, defaults to first category)
     */
    private void handleList(HttpServletRequest request, HttpServletResponse response, String categoryId)
            throws ServletException, IOException {

        try {
            // Get all categories for navigation
            List<StoreCategory> allCategories = StoreCategoryData.getAllCategories();
            request.setAttribute("allCategories", allCategories);

            // If no category specified, default to first category
            if ((categoryId == null || categoryId.isEmpty()) && !allCategories.isEmpty()) {
                categoryId = allCategories.get(0).getId();
            }

            // Always attempt to get currentCategory if categoryId is available (either from param or defaulted)
            StoreCategory currentCategory = null;
            if (categoryId != null && !categoryId.isEmpty()) {
                currentCategory = StoreCategoryData.getCategoryById(categoryId);
            }
            request.setAttribute("currentCategory", currentCategory); // Set currentCategory here

            // If currentCategory is valid, load products
            if (currentCategory != null) { // Use currentCategory here
                // Lazy load products (will use cache if available)
                List<NaverProductDTO> products = storeProductService.getCategoryProducts(categoryId);

                request.setAttribute("products", products);
                request.setAttribute("productCount", products.size());
            } else {
                // If categoryId was provided but invalid, or no categories exist
                if (categoryId != null && !categoryId.isEmpty()) {
                    request.setAttribute("error", "Invalid category: " + categoryId);
                } else if (allCategories.isEmpty()) {
                    request.setAttribute("error", "No categories available.");
                }
            }

            // Always get category counts for navigation
            Map<String, Integer> categoryCounts = storeProductService.getCategoryCounts();
            request.setAttribute("categoryCounts", categoryCounts);

            // Get language preference
            String lang = (String) request.getSession().getAttribute("language");
            if (lang == null) {
                lang = "en"; // Default to English
            }
            request.setAttribute("lang", lang);

            // Forward to JSP
            request.getRequestDispatcher("/store.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error in store list: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error loading store: " + e.getMessage());
            request.getRequestDispatcher("/store.jsp").forward(request, response);
        }
    }

    /**
     * Handle initialize action (admin)
     * URL: /store?action=initialize
     */
    private void handleInitialize(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            System.out.println("Admin action: Initialize all categories");
            storeProductService.initializeAllCategories();

            request.setAttribute("success", "All categories initialized successfully!");
            response.sendRedirect(request.getContextPath() + "/store");

        } catch (Exception e) {
            System.err.println("Error initializing categories: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error initializing categories: " + e.getMessage());
            request.getRequestDispatcher("/store.jsp").forward(request, response);
        }
    }

    /**
     * Handle refresh action (admin)
     * URL: /store?action=refresh&category=upper_body
     */
    private void handleRefresh(HttpServletRequest request, HttpServletResponse response, String categoryId)
            throws ServletException, IOException {

        try {
            if (categoryId == null || categoryId.isEmpty()) {
                request.setAttribute("error", "Category ID required for refresh");
                response.sendRedirect(request.getContextPath() + "/store");
                return;
            }

            System.out.println("Admin action: Refresh category " + categoryId);
            List<NaverProductDTO> products = storeProductService.refreshCategoryProducts(categoryId);

            request.setAttribute("success", "Category " + categoryId + " refreshed! " + products.size() + " products loaded.");
            response.sendRedirect(request.getContextPath() + "/store?category=" + categoryId);

        } catch (Exception e) {
            System.err.println("Error refreshing category: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error refreshing category: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/store?category=" + categoryId);
        }
    }
}
