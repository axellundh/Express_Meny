package app.com.example.axel.express_meny;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Document htmlDocument;
    private String htmlPageUrl;
    private TextView parsedHtmlNode;
    private String htmlContentInStringFormat;
    private String eCourseDesc = "";
    private String vCourseDesc = "";


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {

            TextView course = (TextView) getActivity().findViewById(R.id.express_course_editText);
            updateMenu();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateMenu() {


        ParseURL parseURL = new ParseURL();
        parseURL.execute();

    }

    private String getVegetariskCourse() {
        return null;
    }

    private String getExpressCourse() {
        return null;
    }


    private class ParseURL extends AsyncTask<Void, Void, Void> {

        StringBuffer buffer = new StringBuffer();
        @Override
        protected void onPreExecute() {
            htmlPageUrl =  getString(R.string.menuURL);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                htmlDocument = Jsoup.connect(htmlPageUrl).get();
                htmlContentInStringFormat = htmlDocument.title();

                Elements courses = htmlDocument.select("div.rss_content");
                Element vegitariskCourse;
                boolean expressfound = false;
                Element expressCourse;

                expressCourse = courses.select("[id=rss_express]").parents().first();
                vegitariskCourse = courses.select("[id=rss_express-vegetarisk]").parents().first();
               // If no data is retrieved make sure to not get null pointer exception
                if(expressCourse == null || vegitariskCourse == null){
                    eCourseDesc = "No serving today";
                    vCourseDesc = "No serving today";
                    return null;
                }
                expressCourse = expressCourse.select("span.desc").first();
                vegitariskCourse = vegitariskCourse.select("span.desc").first();
            //    eCourse =expressCourse.select()

                eCourseDesc = expressCourse.text();
                vCourseDesc = vegitariskCourse.text();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            setMenu();
        }
    }

    private void setMenu() {

        TextView textView = (TextView) getActivity().findViewById(R.id.express_course_editText);
        textView.setText(eCourseDesc);
        textView = (TextView) getActivity().findViewById(R.id.vegitarisk_course_editText);
        textView.setText(vCourseDesc);
    }
}
