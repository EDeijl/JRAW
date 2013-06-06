import java.util.Iterator;

/**
 * User: Erik
 * Date: 5-6-13
 * Time: 14:32
 * Copyright (c) 2013 Erik Deijl
 */
public class Listing<T> implements Iterable<T> {

    private Reddit Reddit;
    private String Url;

    Listing(Reddit reddit, String url) {
        Reddit = reddit;
        Url = url;

    }

    /**
     * Returns an iterator over a set of elements of type T.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new ListingIterator<T>(this);  //To change body of implemented methods use File | Settings | File Templates.
    }

    private class ListingIterator<T> implements Iterator<T> {
        private Listing<T> tListing;
        private int CurrentPageIndex;
        private String After;
        private String Before;
        private Thing[] CurrentPage;

        public ListingIterator(Listing<T> listing) {
            tListing = listing;
            CurrentPageIndex = 0;
        }

        public T Current() {
            return (T) CurrentPage[CurrentPageIndex - 1];
        }

        private void FetchNextPage() {
            String url = Listing.this.Url;
            if (After != null) {
                if (url.contains("?"))
                    url += "&after=" + After;
                else
                    url += "?after=" + After;
            }

            //TODO fetching page
        }



        @Override
        public boolean hasNext() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }


        @Override
        public T next() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }


        @Override
        public void remove() {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
