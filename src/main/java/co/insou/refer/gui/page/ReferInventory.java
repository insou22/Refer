package co.insou.refer.gui.page;

/**
 * Created by insou on 17/12/2015.
 */
public interface ReferInventory {

    void displayInventory();

    void setPageItem(int pageNumber, int slot, GUIPageType page);

    GUIPageType getPageItem(int pageNumber, int slot);

    boolean isPageItem(int pageNumber, int slot);

    GUIPageType getPageItem(int slot);

    boolean isPageItem(int slot);

}
