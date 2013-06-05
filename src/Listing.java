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


        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws java.util.NoSuchElementException
         *          if the iteration has no more elements
         */
        @Override
        public T next() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        /**
         * Removes from the underlying collection the last element returned
         * by this iterator (optional operation).  This method can be called
         * only once per call to {@link #next}.  The behavior of an iterator
         * is unspecified if the underlying collection is modified while the
         * iteration is in progress in any way other than by calling this
         * method.
         *
         * @throws UnsupportedOperationException if the {@code remove}
         *                                       operation is not supported by this iterator
         * @throws IllegalStateException         if the {@code next} method has not
         *                                       yet been called, or the {@code remove} method has already
         *                                       been called after the last call to the {@code next}
         *                                       method
         */
        @Override
        public void remove() {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
