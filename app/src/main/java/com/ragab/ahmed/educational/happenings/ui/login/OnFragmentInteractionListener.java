package com.ragab.ahmed.educational.happenings.ui.login;

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p/>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */


/**
 * Created by Ragabov on 3/18/2016.
 */
public interface OnFragmentInteractionListener {
    static final int LAUNCH_SIGN_UP = 1;
    static final int LAUNCH_SIGN_IN = 2;
    static final int FINISH_SIGN_UP = 3;
    static final int FINISH_SIGN_IN = 4;
    // TODO: Update argument type and name
    public void onFragmentInteraction(int REQUEST_CODE);
}

