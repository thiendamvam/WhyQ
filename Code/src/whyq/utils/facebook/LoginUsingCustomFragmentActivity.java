/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package whyq.utils.facebook;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whyq.R;

public class LoginUsingCustomFragmentActivity extends FragmentActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_custom_fragment_activity);
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//    		Bundle savedInstanceState) {
//    	// TODO Auto-generated method stub
//    	
//    	View v = inflater.inflate(R.layout.facebook_custom_fragment_activity, container,false);
//    	return v;
//    }
}

