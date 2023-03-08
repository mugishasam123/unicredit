package it.codeland.sam.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.resource.Resource;

import com.day.cq.wcm.api.Page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { SlingHttpServletRequest.class,
        Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TestModel {

    private final Logger logger = LoggerFactory.getLogger(TestModel.class);

    @ScriptVariable
    private Page currentPage;

    @SlingObject
    private ResourceResolver resourceResolver;

    private List<MenuPojo> menuList = new ArrayList<>();

  
    @PostConstruct
    protected void init() {
        logger.info("***** :: init :: Start :: ******");
        menuList = currentPage != null ? getFirstLevelItems(currentPage, menuList) : null; // Here you can read the
                                                                                           // currentPage path dialog
                                                                                           // also. I am reading it from
                                                                                           // OOTB object.
        logger.info("***** :: init :: End :: *****");
    }

    private List<MenuPojo> getFirstLevelItems(Page rootPage, List<MenuPojo> menuList) {
        Iterator<Page> firstLevelChild = rootPage.listChildren();
        while (firstLevelChild.hasNext()) {
            Page firstLevelPage = firstLevelChild.next();
            if (!firstLevelPage.isHideInNav()) {
                getMenuList(menuList, firstLevelPage);
            }
        }
        return menuList;
    }

    private void getMenuList(List<MenuPojo> menuList, Page firstLevelPage) {
        MenuPojo navObj = new MenuPojo();
        ValueMap firstLevelPageProperties = firstLevelPage.getProperties();
        String pathLink = firstLevelPage.getPath();
        navObj.setPageLink(pathLink);
        navObj.setPageTitle(firstLevelPage.getTitle());
        navObj.setPageName(firstLevelPage.getName());
        if (firstLevelPage.listChildren().hasNext()) {
            List<MenuPojo> secondLevelItemList = new ArrayList<>();
            secondLevelItemList = getFirstLevelItems(firstLevelPage, secondLevelItemList);
            navObj.setChildPages(secondLevelItemList);
        }
        menuList.add(navObj);
    }

    public List<MenuPojo> getMenuList() {
        return menuList;
    }







    public class MenuPojo {

        private String pageTitle;
        private String pageName;
        private String pageLink;
        private List<MenuPojo> childPages;

        public String getPageTitle() {
            return pageTitle;
        }

        public String getPageName() {
            return pageName;
        }

        public void setPageTitle(String pageTitle) {
            this.pageTitle = pageTitle;
        }
        public void setPageName(String pageName) {
            this.pageName = pageName;
        }

        public String getPageLink() {
            return pageLink;
        }

        public void setPageLink(String pageLink) {
            this.pageLink = pageLink;
        }

        public List<MenuPojo> getChildPages() {
            return childPages;
        }

        public void setChildPages(List<MenuPojo> childPages) {
            this.childPages = childPages;
        }

    }

}