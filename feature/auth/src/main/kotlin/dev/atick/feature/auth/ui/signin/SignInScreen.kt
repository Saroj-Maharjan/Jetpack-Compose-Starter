/*
 * Copyright 2023 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.atick.feature.auth.ui.signin

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.atick.core.extensions.getActivity
import dev.atick.core.ui.components.JetpackButton
import dev.atick.core.ui.components.JetpackOutlinedButton
import dev.atick.core.ui.components.JetpackPasswordFiled
import dev.atick.core.ui.components.JetpackTextButton
import dev.atick.core.ui.components.JetpackTextFiled
import dev.atick.core.ui.utils.PreviewDevices
import dev.atick.core.ui.utils.PreviewThemes
import dev.atick.core.ui.utils.SnackbarAction
import dev.atick.core.ui.utils.StatefulComposable
import dev.atick.feature.auth.R

/**
 * Sign in route.
 *
 * @param onSignUpClick Navigate to sign up screen.
 * @param onShowSnackbar Show snackbar.
 * @param signInViewModel [SignInViewModel].
 */
@Composable
fun SignInRoute(
    onSignUpClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    signInViewModel: SignInViewModel = hiltViewModel(),
) {
    val signInState by signInViewModel.signInUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = signInState,
        onShowSnackbar = onShowSnackbar,
    ) { homeScreenData ->
        SignInScreen(
            homeScreenData,
            signInViewModel::updateEmail,
            signInViewModel::updatePassword,
            signInViewModel::signInWithSavedCredentials,
            signInViewModel::signInWithGoogle,
            signInViewModel::loginWithEmailAndPassword,
            onSignUpClick,
        )
    }
}

/**
 * Sign in screen.
 *
 * @param screenData [SignInScreenData].
 * @param onEmailChange Callback when email is changed.
 * @param onPasswordChange Callback when password is changed.
 * @param onSignInWithSavedCredentials Callback when sign in with saved credentials is clicked.
 * @param onSignInWithGoogleClick Callback when sign in with Google is clicked.
 * @param onSignInClick Callback when sign in is clicked.
 * @param onSignUpClick Callback when sign up is clicked.
 */
@Composable
private fun SignInScreen(
    screenData: SignInScreenData,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInWithSavedCredentials: (Activity) -> Unit,
    onSignInWithGoogleClick: (Activity) -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val activity = LocalContext.current.getActivity()

    // Try sign in with saved credentials on launch
    LaunchedEffect(Unit, onSignInWithSavedCredentials) {
        activity?.run(onSignInWithSavedCredentials)
    }

    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        Text(stringResource(R.string.sign_in), style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))
        JetpackOutlinedButton(
            onClick = { activity?.run(onSignInWithGoogleClick) },
            text = { Text(text = "Sign In with Google") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(56.dp),
        )
        Text(text = "or", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        JetpackTextFiled(
            value = screenData.email.value,
            errorMessage = screenData.email.errorMessage,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(R.string.email),
                )
            },
        )
        JetpackPasswordFiled(
            value = screenData.password.value,
            errorMessage = screenData.password.errorMessage,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.password)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = stringResource(R.string.password),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        JetpackButton(
            onClick = {
                focusManager.clearFocus()
                onSignInClick.invoke()
            },
            modifier = Modifier.fillMaxWidth(),
            text = { Text(stringResource(R.string.sign_in)) },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(R.string.do_not_have_an_account))
            JetpackTextButton(onClick = onSignUpClick) {
                Text(
                    text = stringResource(R.string.sign_up),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
@PreviewThemes
@PreviewDevices
private fun SignInScreenPreview() {
    SignInScreen(
        screenData = SignInScreenData(),
        onEmailChange = {},
        onPasswordChange = {},
        onSignInWithSavedCredentials = {},
        onSignInWithGoogleClick = {},
        onSignInClick = {},
        onSignUpClick = {},
    )
}
