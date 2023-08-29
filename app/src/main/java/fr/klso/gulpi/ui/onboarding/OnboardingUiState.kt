package fr.klso.gulpi.ui.onboarding

data class OnboardingUiState(
    val isLoading: Boolean = false,
    val isMalformedUrl: Boolean = false,
    val isEndpointInvalid: Boolean = false,
)
