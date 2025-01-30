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

package dev.atick.compose.repository.home

import javax.inject.Inject

/**
 * Implementation of the [HomeRepository] interface.
 *
 * This class is responsible for providing the data related to the Home feature.
 * It is injected using Dagger's @Inject annotation.
 */
class HomeRepositoryImpl @Inject constructor() : HomeRepository
