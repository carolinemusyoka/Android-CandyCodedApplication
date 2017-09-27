package com.codeschool.candycoded;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@PrepareForTest({AppCompatActivity.class, Uri.class, Intent.class})
@RunWith(PowerMockRunner.class)
public class Task2Tests {

    private static InfoActivity infoActivity;
    //private static Uri mockUri = mock(Uri.class);
    private static boolean called_startActivity = false;
    private static boolean called_uri_parse = false;
    private static boolean created_intent = false;
    private static boolean created_intent_correctly = false;

    // Mockito setup
    @BeforeClass
    public static void setup() throws Exception {
        // Spy on a MainActivity instance.
        infoActivity = PowerMockito.spy(new InfoActivity());
        // Create a fake Bundle to pass in.
        Bundle bundle = mock(Bundle.class);
        Uri mockUri = mock(Uri.class);
        //Uri actualUri = Uri.parse("geo:0,0?q=618 E South St Orlando, FL 32801");
        //Uri spyUri = PowerMockito.spy(Uri.parse("geo:0,0?q=618 E South St Orlando, FL 32801"));
        Intent intent = PowerMockito.spy(new Intent(Intent.ACTION_VIEW, mockUri));

        try {
            // Do not allow super.onCreate() to be called, as it throws errors before the user's code.
            PowerMockito.suppress(PowerMockito.methodsDeclaredIn(AppCompatActivity.class));

            // Return a mocked Intent from the call to its constructor.
//            Intent mockMapIntent = mock(Intent.class);
//
//            PowerMockito.whenNew(Intent.class).withParameterTypes(
//                    String.class,
//                    Uri.class)
//                    .withArguments(Mockito.any(String.class), Mockito.any(Uri.class)).thenReturn(mockMapIntent);

            PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intent);

            try {
                infoActivity.onCreate(bundle);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Creating the mockUri - can we check if mockUri has this info?
            //when(Uri.parse("geo:0,0?q=618 E South St Orlando, FL 32801")).thenReturn(mockUri);
            PowerMockito.mockStatic(Uri.class);
            infoActivity.createMapIntent(null);
            PowerMockito.verifyStatic(Uri.class);
            Uri.parse("geo:0,0?q=618 E South St Orlando, FL 32801"); // This has to come on the line after mockStatic
            //when(Uri.parse("geo:0,0?q=618 E South St Orlando, FL 32801")).thenReturn(actualUri);
            called_uri_parse = true;

            try {
                PowerMockito.verifyNew(Intent.class, Mockito.atLeastOnce()).withArguments(Mockito.eq(Intent.ACTION_VIEW), Mockito.any());
                created_intent = true;
            } catch (Throwable e) {
                e.printStackTrace();
            }




        } catch (Throwable e) {
            e.printStackTrace();

            // Need to replace doThrow() above with using verify on startActivity - see aj's code
            //called_startActivity = true;
        }
        //Mockito.verify(infoActivity).startActivity(mapIntent);
    }

    @Test
    public void t2_1_createMapIntent_Exists() throws Exception {
        Class<?> myClass = null;

        try {
            myClass =  InfoActivity.class
                    .getMethod("createMapIntent", View.class)
                    .getDeclaringClass();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        assertEquals(myClass, InfoActivity.class);
    }

    @Test
    public void t2_2_createGeoUri() throws Exception {
        // Was hoping we could check that the mockUri is equivalent to this one below
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=618 E South St Orlando, FL 32801");

        //assertNotNull(mockUri);
        assertTrue(called_uri_parse);
    }

    @Test
    public void t2_3_createIntent() throws Exception {
        assertTrue(created_intent);
    }

    //@Test
    //public void t2_3_callStartActivity() throws Exception {
    //    assertTrue(called_startActivity);
    //}


}
