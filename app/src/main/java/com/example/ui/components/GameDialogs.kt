package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import android.graphics.Paint
import android.graphics.Path as AndroidPath
import android.graphics.Typeface
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.game.BoosterType
import com.example.ui.theme.AlertRed
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.HudBlue
import com.example.ui.theme.HudBlueDark
import com.example.ui.theme.LilitaOneFontFamily
import com.example.ui.theme.OutlineInk
import com.example.ui.theme.PlayGreen
import com.example.ui.theme.PlayGreenDark
import com.example.ui.theme.ShopPurple
import com.example.ui.theme.ShopPurpleDark
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun GlossyButton(
  text: String,
  topColor: Color,
  bottomColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  testTag: String? = null,
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(if (isPressed) 0.92f else 1f, spring(), label = "btn_scale")

  Box(
    modifier =
      modifier
        .scale(scale)
        .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
        .clip(RoundedCornerShape(18.dp))
        .background(bottomColor)
        .padding(bottom = 5.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(topColor)
        .border(2.5.dp, OutlineInk, RoundedCornerShape(16.dp))
        .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
        .padding(horizontal = 18.dp, vertical = 12.dp),
    contentAlignment = Alignment.Center,
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
      if (icon != null) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(6.dp))
      }
      Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(fontFamily = FredokaFontFamily, fontSize = 16.sp),
        color = Color.White,
        maxLines = 1,
      )
    }
  }
}

@Composable
fun CoinPill(
  coins: Int,
  onAddClick: () -> Unit,
  modifier: Modifier = Modifier,
  pillWidth: Dp = 132.dp,
) {
  val visualHeight = pillWidth * (108f / 363f)
  val touchHeight = maxOf(visualHeight, 48.dp)
  Box(
    modifier =
      modifier
        .width(pillWidth)
        .height(touchHeight)
        .clickable(onClick = onAddClick)
        .testTag("coin_pill"),
    contentAlignment = Alignment.Center,
  ) {
    Box(
      modifier = Modifier.width(pillWidth).height(visualHeight),
      contentAlignment = Alignment.Center,
    ) {
      Image(
        painter = painterResource(id = R.drawable.btn_hud_coin),
        contentDescription = "Coins: $coins",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize(),
      )
      val baseSize =
        if (pillWidth < 120.dp) {
          if (coins >= 1000) 11.5.sp else 13.5.sp
        } else {
          if (coins >= 1000) 13.sp else 16.sp
        }
      Text(
        text = coins.toString(),
        style =
          MaterialTheme.typography.labelLarge.copy(
            fontFamily = FredokaFontFamily,
            fontSize = baseSize,
            fontWeight = FontWeight.Bold,
            shadow = Shadow(color = OutlineInk, offset = Offset(0f, 2f), blurRadius = 2f),
          ),
        color = Color.White,
        maxLines = 1,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(start = pillWidth * 0.27f, end = pillWidth * 0.25f),
      )
    }
  }
}

@Composable
fun CartoonPopupCard(
  title: String,
  headerColor: Color = HudBlue,
  onDismiss: () -> Unit,
  content: @Composable () -> Unit,
) {
  Dialog(onDismissRequest = onDismiss) {
    Column(
      modifier =
        Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(26.dp))
          .background(Color.White)
          .border(3.dp, OutlineInk, RoundedCornerShape(26.dp)),
    ) {
      Box(
        modifier =
          Modifier
            .fillMaxWidth()
            .background(headerColor)
            .padding(vertical = 14.dp, horizontal = 18.dp),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleLarge.copy(fontFamily = FredokaFontFamily),
          color = Color.White,
          textAlign = TextAlign.Center,
        )
      }
      Column(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        content()
      }
    }
  }
}

@Composable
fun WinLevelDialog(
  levelNumber: Int,
  @Suppress("UNUSED_PARAMETER") stars: Int,
  onNextLevel: () -> Unit,
  onClaimBonusAndNext: () -> Unit = onNextLevel,
  @Suppress("UNUSED_PARAMETER") onHome: () -> Unit = {},
  modifier: Modifier = Modifier,
) {
  val backdropInteraction = remember { MutableInteractionSource() }
  val cardInteraction = remember { MutableInteractionSource() }
  val cardPopScale = remember { Animatable(0.76f) }

  LaunchedEffect(Unit) {
    cardPopScale.animateTo(
      targetValue = 1f,
      animationSpec =
        spring(
          dampingRatio = Spring.DampingRatioMediumBouncy,
          stiffness = Spring.StiffnessMedium,
        ),
    )
  }

  BoxWithConstraints(
    modifier =
      modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.85f))
        .clickable(
          interactionSource = backdropInteraction,
          indication = null,
          onClick = {},
        )
        .windowInsetsPadding(WindowInsets.statusBars)
        .windowInsetsPadding(WindowInsets.displayCutout)
        .windowInsetsPadding(WindowInsets.navigationBars)
        .padding(horizontal = 16.dp, vertical = 16.dp)
        .testTag("win_dim_backdrop"),
    contentAlignment = Alignment.Center,
  ) {
    val cardAspect = 408f / 605f
    val targetCardW = minOf(maxWidth * 0.88f, 365.dp, (maxHeight * 0.90f) * cardAspect)
    BoxWithConstraints(
      modifier =
        Modifier
          .width(targetCardW)
          .aspectRatio(cardAspect)
          .scale(cardPopScale.value)
          .clickable(
            interactionSource = cardInteraction,
            indication = null,
            onClick = {},
          )
          .testTag("win_level_dialog"),
    ) {
      val cardW = maxWidth
      val cardH = maxHeight

      Image(
        painter = painterResource(id = R.drawable.popup_level_win),
        contentDescription = "Level $levelNumber Complete",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize(),
      )

      // Crisp "Perfect!" text extracted from reference card, centered between the two sparkles
      val perfectW = cardW * 0.465f
      val perfectH = perfectW * (228f / 798f)
      Image(
        painter = painterResource(id = R.drawable.txt_win_perfect),
        contentDescription = stringResource(R.string.win_perfect_title),
        contentScale = ContentScale.Fit,
        modifier =
          Modifier
            .align(Alignment.TopCenter)
            .offset(y = cardH * 0.258f - perfectH * 0.5f)
            .size(width = perfectW, height = perfectH)
            .testTag("win_perfect_logo"),
      )

      // Golden reward panel with "+20" gold coin pile centered below Perfect logo
      val rewardPanelW = cardW * 0.78f
      val rewardPanelH = rewardPanelW * (358f / 596f)
      val coinsH = rewardPanelH * 0.84f
      val coinsW = coinsH * (562f / 396f)
      Box(
        modifier =
          Modifier
            .align(Alignment.TopCenter)
            .offset(y = cardH * 0.382f)
            .size(width = rewardPanelW, height = rewardPanelH)
            .testTag("win_reward_panel"),
        contentAlignment = Alignment.Center,
      ) {
        Image(
          painter = painterResource(id = R.drawable.panel_win_reward),
          contentDescription = null,
          contentScale = ContentScale.Fit,
          modifier = Modifier.fillMaxSize(),
        )
        Image(
          painter = painterResource(id = R.drawable.img_win_coins_20),
          contentDescription = "+20 Coins Reward",
          contentScale = ContentScale.Fit,
          modifier =
            Modifier
              .offset(y = rewardPanelH * 0.015f)
              .size(width = coinsW, height = coinsH)
              .testTag("win_reward_coins"),
        )
      }

      // Red "NEXT" and blue "Getx20 400" buttons side-by-side below the reward panel like reference
      val btnW = cardW * 0.405f
      val btnGap = cardW * 0.028f
      val nextH = btnW * (180f / 425f)
      val getBonusH = btnW * (181f / 425f)
      val btnTouchH = maxOf(nextH, getBonusH, 48.dp)

      val nextInteraction = remember { MutableInteractionSource() }
      val nextPressed by nextInteraction.collectIsPressedAsState()
      val nextScale by animateFloatAsState(
        if (nextPressed) 0.92f else 1f,
        spring(),
        label = "win_next_scale",
      )

      val getBonusInteraction = remember { MutableInteractionSource() }
      val getBonusPressed by getBonusInteraction.collectIsPressedAsState()
      val getBonusScale by animateFloatAsState(
        if (getBonusPressed) 0.92f else 1f,
        spring(),
        label = "win_get_bonus_scale",
      )

      Row(
        modifier =
          Modifier
            .align(Alignment.TopCenter)
            .offset(y = cardH * 0.822f - btnTouchH * 0.5f)
            .width(btnW * 2f + btnGap)
            .height(btnTouchH),
        horizontalArrangement = Arrangement.spacedBy(btnGap),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Box(
          modifier =
            Modifier
              .size(width = btnW, height = btnTouchH)
              .scale(nextScale)
              .clickable(
                interactionSource = nextInteraction,
                indication = null,
                onClick = onNextLevel,
              )
              .testTag("win_next_button"),
          contentAlignment = Alignment.Center,
        ) {
          Image(
            painter = painterResource(id = R.drawable.btn_win_next),
            contentDescription = stringResource(R.string.win_next_button),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(width = btnW, height = nextH),
          )
        }

        Box(
          modifier =
            Modifier
              .size(width = btnW, height = btnTouchH)
              .scale(getBonusScale)
              .clickable(
                interactionSource = getBonusInteraction,
                indication = null,
                onClick = onClaimBonusAndNext,
              )
              .testTag("win_get_bonus_button"),
          contentAlignment = Alignment.Center,
        ) {
          Image(
            painter = painterResource(id = R.drawable.btn_win_get_bonus),
            contentDescription = stringResource(R.string.win_get_bonus_button),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(width = btnW, height = getBonusH),
          )
        }
      }

      val context = LocalContext.current
      val lilitaTypeface = remember(context) {
        ResourcesCompat.getFont(context, R.font.lilita_one) ?: Typeface.DEFAULT_BOLD
      }

      Canvas(
        modifier =
          Modifier
            .fillMaxSize()
            .semantics { contentDescription = "LEVEL $levelNumber" }
            .testTag("win_level_ribbon_title"),
      ) {
        val w = size.width
        val h = size.height
        val label = "LEVEL $levelNumber"

        val basePaint =
          Paint(Paint.ANTI_ALIAS_FLAG).apply {
            typeface = lilitaTypeface
            textAlign = Paint.Align.CENTER
            letterSpacing = 0.02f
          }

        // Fit cleanly between the two yellow stars & sparkles on the ribbon
        var textSizePx = w * 0.118f
        basePaint.textSize = textSizePx
        val maxRibbonTextW = w * 0.41f
        val measured = basePaint.measureText(label)
        if (measured > maxRibbonTextW && measured > 0f) {
          textSizePx *= (maxRibbonTextW / measured)
          basePaint.textSize = textSizePx
        }

        // Gentle convex upward arch following the blue ribbon contour
        val arcStartX = w * 0.25f
        val arcEndX = w * 0.75f
        val endBaselineY = h * 0.104f
        val midBaselineY = h * 0.097f
        val ctrlY = 2f * midBaselineY - endBaselineY
        val ribbonArcPath =
          AndroidPath().apply {
            moveTo(arcStartX, endBaselineY)
            quadTo(w * 0.50f, ctrlY, arcEndX, endBaselineY)
          }

        val shadowPaint =
          Paint(basePaint).apply {
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = textSizePx * 0.20f
            color = Color(0xFF031845).toArgb()
          }
        val outlinePaint =
          Paint(basePaint).apply {
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = textSizePx * 0.18f
            color = Color(0xFF062666).toArgb()
          }
        val fillPaint =
          Paint(basePaint).apply {
            style = Paint.Style.FILL
            color = android.graphics.Color.WHITE
          }

        drawIntoCanvas { canvas ->
          val native = canvas.nativeCanvas
          native.drawTextOnPath(label, ribbonArcPath, 0f, h * 0.0045f, shadowPaint)
          native.drawTextOnPath(label, ribbonArcPath, 0f, 0f, outlinePaint)
          native.drawTextOnPath(label, ribbonArcPath, 0f, 0f, fillPaint)
        }
      }
    }
  }
}

@Composable
fun LoseLevelDialog(
  coins: Int,
  notice: String?,
  onRetry: () -> Unit,
  onRevive: () -> Unit,
  onHome: () -> Unit,
) {
  CartoonPopupCard(title = "LEVEL FAILED", headerColor = AlertRed, onDismiss = {}) {
    Text(
      text = "All waiting slots are full and no parked bus matches the next passenger!",
      style = MaterialTheme.typography.bodyLarge,
      color = OutlineInk,
      textAlign = TextAlign.Center,
    )
    if (notice != null) {
      Spacer(Modifier.height(8.dp))
      Text(
        text = notice,
        style = MaterialTheme.typography.labelLarge,
        color = AlertRed,
      )
    }
    Spacer(Modifier.height(16.dp))
    GlossyButton(
      text = "Revive (+1 Slot) • 200",
      topColor = GoldCoin,
      bottomColor = GoldCoinDark,
      onClick = onRevive,
      modifier = Modifier.fillMaxWidth(),
      testTag = "revive_button",
    )
    Spacer(Modifier.height(10.dp))
    GlossyButton(
      text = "Retry Level",
      topColor = HudBlue,
      bottomColor = HudBlueDark,
      onClick = onRetry,
      modifier = Modifier.fillMaxWidth(),
      testTag = "retry_button",
    )
    Spacer(Modifier.height(10.dp))
    GlossyButton(
      text = "Home",
      topColor = Color(0xFF7B8BA3),
      bottomColor = Color(0xFF566376),
      onClick = onHome,
      modifier = Modifier.fillMaxWidth(),
      testTag = "lose_home_button",
    )
    Spacer(Modifier.height(8.dp))
    Text(
      text = "Your Coins: $coins",
      style = MaterialTheme.typography.labelSmall,
      color = Color(0xFF5C6C84),
    )
  }
}

@Composable
fun PauseDialog(
  soundEnabled: Boolean,
  vibrationEnabled: Boolean,
  cloudSaveEnabled: Boolean,
  languageLabel: String,
  onToggleSound: (Boolean) -> Unit,
  onToggleVibration: (Boolean) -> Unit,
  onToggleCloudSave: (Boolean) -> Unit,
  onCycleLanguage: () -> Unit,
  onOpenSupport: () -> Unit,
  onResume: () -> Unit,
  onRestart: () -> Unit,
  onHome: () -> Unit,
  modifier: Modifier = Modifier,
) {
  SettingsPopupOverlay(
    soundEnabled = soundEnabled,
    vibrationEnabled = vibrationEnabled,
    cloudSaveEnabled = cloudSaveEnabled,
    languageLabel = languageLabel,
    onToggleSound = onToggleSound,
    onToggleVibration = onToggleVibration,
    onToggleCloudSave = onToggleCloudSave,
    onCycleLanguage = onCycleLanguage,
    onOpenSupport = onOpenSupport,
    onDismiss = onResume,
    onRestart = onRestart,
    onHome = onHome,
    modifier = modifier,
  )
}

@Composable
fun SettingsPopupOverlay(
  soundEnabled: Boolean,
  vibrationEnabled: Boolean,
  cloudSaveEnabled: Boolean,
  languageLabel: String,
  onToggleSound: (Boolean) -> Unit,
  onToggleVibration: (Boolean) -> Unit,
  onToggleCloudSave: (Boolean) -> Unit,
  onCycleLanguage: () -> Unit,
  onOpenSupport: () -> Unit,
  onDismiss: () -> Unit,
  onRestart: () -> Unit,
  onHome: (() -> Unit)? = null,
  modifier: Modifier = Modifier,
) {
  val backdropInteraction = remember { MutableInteractionSource() }
  val cardInteraction = remember { MutableInteractionSource() }
  val cardPopScale = remember { Animatable(0.76f) }
  var showLevelsPanel by remember { mutableStateOf(false) }

  BackHandler(enabled = showLevelsPanel) {
    showLevelsPanel = false
  }

  LaunchedEffect(Unit) {
    cardPopScale.animateTo(
      targetValue = 1f,
      animationSpec =
        spring(
          dampingRatio = Spring.DampingRatioMediumBouncy,
          stiffness = Spring.StiffnessMedium,
        ),
    )
  }

  val isHomeSettings = onHome == null
  BoxWithConstraints(
    modifier =
      modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = if (isHomeSettings) 0.78f else 0.87f))
        .clickable(
          interactionSource = backdropInteraction,
          indication = null,
          onClick = onDismiss,
        )
        .windowInsetsPadding(WindowInsets.statusBars)
        .windowInsetsPadding(WindowInsets.displayCutout)
        .windowInsetsPadding(WindowInsets.navigationBars)
        .padding(horizontal = 8.dp, vertical = 10.dp)
        .testTag("settings_dim_backdrop"),
    contentAlignment = Alignment.Center,
  ) {
    val cardAspect = if (isHomeSettings) 950f / 1050f else 858f / 1154f
    val targetCardW =
      if (isHomeSettings) {
        minOf(maxWidth * 0.85f, 390.dp, (maxHeight * 0.56f) * cardAspect)
      } else {
        minOf(maxWidth * 0.94f, 420.dp, (maxHeight * 0.90f) * cardAspect)
      }
    BoxWithConstraints(
      modifier =
        Modifier
          .width(targetCardW)
          .aspectRatio(cardAspect)
          .scale(cardPopScale.value)
          .clickable(
            interactionSource = cardInteraction,
            indication = null,
            onClick = {},
          )
          .testTag("settings_pause_dialog"),
    ) {
      val cardW = maxWidth
      val cardH = maxHeight

      Image(
        painter =
          painterResource(
            id = if (isHomeSettings) R.drawable.popup_settings_home else R.drawable.popup_settings,
          ),
        contentDescription = "Settings",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize(),
      )

      // Top-right glossy red 'X' close button protruding cleanly outward on the ribbon corner
      val closeTouchSize = maxOf(48.dp, cardW * 0.165f)
      val closeVisualSize = cardW * 0.142f
      val closeInteraction = remember { MutableInteractionSource() }
      val closePressed by closeInteraction.collectIsPressedAsState()
      val closeScale by animateFloatAsState(if (closePressed) 0.90f else 1f, spring(), label = "settings_close_scale")

      Box(
        modifier =
          Modifier
            .offset(
              x = cardW * (if (isHomeSettings) 0.956f else 0.946f) - closeTouchSize / 2f,
              y = cardH * (if (isHomeSettings) 0.169f else 0.114f) - closeTouchSize / 2f,
            )
            .size(closeTouchSize)
            .scale(closeScale)
            .clickable(
              interactionSource = closeInteraction,
              indication = null,
              onClick = onDismiss,
            )
            .semantics { contentDescription = "Close Settings" }
            .testTag("settings_close_button"),
        contentAlignment = Alignment.Center,
      ) {
        Box(
          modifier =
            Modifier
              .size(closeVisualSize + 3.dp)
              .clip(CircleShape)
              .background(Color(0xFF131D3A)),
          contentAlignment = Alignment.Center,
        ) {
          Image(
            painter = painterResource(id = R.drawable.btn_settings_close),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(closeVisualSize),
          )
        }
      }

      // Off-white/cream rounded tray behind the 3 top toggle icons (matching reference)
      val trayW = cardW * 0.78f
      val trayH = cardH * 0.146f
      val trayTopY = cardH * 0.184f
      if (!isHomeSettings) {
        Canvas(
          modifier =
            Modifier
              .offset(x = (cardW - trayW) * 0.5f, y = trayTopY)
              .size(width = trayW, height = trayH)
              .testTag("settings_top_cream_tray"),
        ) {
          val w = size.width
          val h = size.height
          val corner = CornerRadius(h * 0.18f, h * 0.18f)
          val shadowDrop = 3.dp.toPx()
          val bevelPx = 2.5.dp.toPx()
          val insetPx = 1.5.dp.toPx()

          // Subtle drop shadow under tray
          drawRoundRect(
            color = Color(0x42022966),
            topLeft = Offset(0f, shadowDrop),
            size = Size(w, h),
            cornerRadius = corner,
          )
          // Warm peach-cream bottom bevel
          drawRoundRect(
            color = Color(0xFFE5B47A),
            topLeft = Offset.Zero,
            size = Size(w, h),
            cornerRadius = corner,
          )
          // Bright off-white top highlight rim
          drawRoundRect(
            color = Color(0xFFFFF6EA),
            topLeft = Offset.Zero,
            size = Size(w, h - bevelPx),
            cornerRadius = corner,
          )
          // Soft warm cream interior
          drawRoundRect(
            brush =
              Brush.verticalGradient(
                colors = listOf(Color(0xFFFCECD6), Color(0xFFF9E2C2)),
              ),
            topLeft = Offset(insetPx, insetPx),
            size = Size(w - insetPx * 2f, h - insetPx - bevelPx),
            cornerRadius = CornerRadius(maxOf(4f, corner.x - insetPx), maxOf(4f, corner.y - insetPx)),
          )

          // Faint decorative stars inside the cream tray matching reference
          val starSpots =
            listOf(
              Triple(w * 0.08f, h * 0.24f, h * 0.09f),
              Triple(w * 0.35f, h * 0.18f, h * 0.065f),
              Triple(w * 0.63f, h * 0.80f, h * 0.07f),
              Triple(w * 0.91f, h * 0.26f, h * 0.085f),
            )
          for ((sx, sy, sr) in starSpots) {
            val starPath =
              Path().apply {
                for (p in 0 until 10) {
                  val r = if (p % 2 == 0) sr else sr * 0.46f
                  val angle = -PI / 2.0 + p * (PI / 5.0)
                  val px = sx + (r * cos(angle)).toFloat()
                  val py = sy + (r * sin(angle)).toFloat()
                  if (p == 0) moveTo(px, py) else lineTo(px, py)
                }
                close()
              }
            drawPath(starPath, color = Color(0xFFF1CFA4).copy(alpha = 0.55f))
          }
        }
      }

      // Top row: Slot 1 = Volume, Slot 2 = Vibrate, Slot 3 = Guide
      val iconBoxW = cardW * (if (isHomeSettings) 0.205f else 0.170f)
      val iconBoxH = maxOf(48.dp, if (isHomeSettings) cardH * 0.140f else iconBoxW * 0.94f)
      val iconTopY =
        if (isHomeSettings) {
          cardH * 0.249f
        } else {
          trayTopY + (trayH - iconBoxH) * 0.5f
        }
      val slot1X = cardW * (if (isHomeSettings) 0.150f else 0.175f)
      val slot2X = cardW * (if (isHomeSettings) 0.3975f else 0.415f)
      val slot3X = cardW * (if (isHomeSettings) 0.645f else 0.655f)

      // 1. Volume (ON / OFF muted speaker)
      SettingsSpriteButton(
        drawableRes = if (soundEnabled) R.drawable.btn_settings_sound_on else R.drawable.btn_settings_sound_off,
        contentDescription = if (soundEnabled) "Sound On" else "Sound Off",
        onClick = { onToggleSound(!soundEnabled) },
        testTag = "settings_sound_toggle",
        modifier =
          Modifier
            .offset(x = slot1X, y = iconTopY)
            .size(width = iconBoxW, height = iconBoxH),
      )

      // 2. Vibrate (diagonal red slash overlay when toggled OFF)
      Box(
        modifier =
          Modifier
            .offset(x = slot2X, y = iconTopY)
            .size(width = iconBoxW, height = iconBoxH),
        contentAlignment = Alignment.Center,
      ) {
        SettingsSpriteButton(
          drawableRes = R.drawable.btn_settings_vibrate,
          contentDescription = if (vibrationEnabled) "Vibration On" else "Vibration Off",
          onClick = { onToggleVibration(!vibrationEnabled) },
          testTag = "settings_vibration_toggle",
          modifier = Modifier.fillMaxSize(),
        )
        if (!vibrationEnabled) {
          androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize(0.72f)) {
            drawLine(
              color = AlertRed,
              start = Offset(size.width * 0.14f, size.height * 0.86f),
              end = Offset(size.width * 0.86f, size.height * 0.14f),
              strokeWidth = size.minDimension * 0.12f,
              cap = StrokeCap.Round,
            )
          }
        }
      }

      // 3. Guide / Tropical Themes Panel icon (opens sliding inner panel)
      SettingsSpriteButton(
        drawableRes = R.drawable.btn_settings_guide,
        contentDescription = "Themes & Stages",
        onClick = { showLevelsPanel = true },
        testTag = "settings_guide_button",
        modifier =
          Modifier
            .offset(x = slot3X, y = iconTopY)
            .size(width = iconBoxW, height = iconBoxH),
      )

      // Middle vertical stack: Home + Replay + Support + Language in-game, Support + Language on Home
      val stackBtnW = cardW * (if (isHomeSettings) 0.44f else 0.395f)
      val stackBtnH = maxOf(48.dp, stackBtnW * (146f / 394f))
      val stackX = (cardW - stackBtnW) * 0.5f

      if (onHome != null) {
        SettingsSpriteButton(
          drawableRes = R.drawable.btn_settings_home,
          contentDescription = "Home",
          onClick = onHome,
          testTag = "pause_home_button",
          modifier =
            Modifier
              .offset(x = stackX, y = cardH * 0.352f)
              .size(width = stackBtnW, height = stackBtnH),
        )

        SettingsSpriteButton(
          drawableRes = R.drawable.btn_settings_replay,
          contentDescription = "Replay Level",
          onClick = onRestart,
          testTag = "restart_button",
          modifier =
            Modifier
              .offset(x = stackX, y = cardH * 0.471f)
              .size(width = stackBtnW, height = stackBtnH),
        )
      }

      val supportY = if (isHomeSettings) cardH * 0.462f else cardH * 0.590f
      val langY = if (isHomeSettings) cardH * 0.632f else cardH * 0.709f

      SettingsSpriteButton(
        drawableRes = R.drawable.btn_settings_support,
        contentDescription = "Support and Help",
        onClick = onOpenSupport,
        testTag = "settings_support_button",
        modifier =
          Modifier
            .offset(x = stackX, y = supportY)
            .size(width = stackBtnW, height = stackBtnH),
      )

      Box(
        modifier =
          Modifier
            .offset(x = stackX, y = langY)
            .size(width = stackBtnW, height = stackBtnH),
        contentAlignment = Alignment.Center,
      ) {
        SettingsSpriteButton(
          drawableRes = R.drawable.btn_settings_language,
          contentDescription = "Language: $languageLabel",
          onClick = onCycleLanguage,
          testTag = "settings_language_button",
          modifier = Modifier.fillMaxSize(),
        )
        if (languageLabel != "English") {
          Box(
            modifier =
              Modifier
                .width(stackBtnW * 0.80f)
                .height(stackBtnH * 0.68f)
                .clip(RoundedCornerShape(14.dp))
                .background(ShopPurple),
            contentAlignment = Alignment.Center,
          ) {
            Text(
              text = languageLabel,
              style =
                MaterialTheme.typography.titleMedium.copy(
                  fontFamily = FredokaFontFamily,
                  fontWeight = FontWeight.ExtraBold,
                  shadow = Shadow(color = OutlineInk, offset = Offset(0f, 2f), blurRadius = 2f),
                ),
              color = Color.White,
              maxLines = 1,
            )
          }
        }
      }

      // Bottom row: Cloud Save label + ON / OFF toggle button positioned cleanly above bottom rim
      val cloudBtnW = cardW * 0.205f
      val cloudBtnVisualH = cloudBtnW * (131f / 194f)
      val cloudRowH = maxOf(48.dp, cloudBtnVisualH)
      val cloudCenterY = cardH * (if (isHomeSettings) 0.868f else 0.884f)
      Row(
        modifier =
          Modifier
            .offset(x = cardW * 0.130f, y = cloudCenterY - cloudRowH * 0.5f)
            .width(cardW * 0.740f)
            .height(cloudRowH),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Image(
          painter = painterResource(id = R.drawable.lbl_settings_cloud_save),
          contentDescription = "Cloud Save",
          contentScale = ContentScale.Fit,
          modifier = Modifier.weight(0.62f).height(cloudRowH * 0.64f),
        )
        Spacer(Modifier.width(cardW * 0.03f))
        SettingsSpriteButton(
          drawableRes = if (cloudSaveEnabled) R.drawable.btn_settings_cloud_on else R.drawable.btn_settings_cloud_off,
          contentDescription = if (cloudSaveEnabled) "Cloud Save On" else "Cloud Save Off",
          onClick = { onToggleCloudSave(!cloudSaveEnabled) },
          testTag = "settings_cloud_save_toggle",
          modifier = Modifier.width(cloudBtnW).height(cloudRowH),
        )
      }

      // Inner expanding/sliding Level/Theme Selection Panel triggered by top-right Volume/Vibrate companion button
      Box(
        modifier =
          Modifier
            .align(Alignment.TopCenter)
            .offset(y = cardH * (if (isHomeSettings) 0.165f else 0.135f))
            .size(width = cardW * 0.88f, height = cardH * (if (isHomeSettings) 0.780f else 0.720f))
            .clip(RoundedCornerShape(cardW * 0.04f)),
        contentAlignment = Alignment.BottomCenter,
      ) {
        AnimatedVisibility(
          visible = showLevelsPanel,
          enter =
            slideInVertically(
              animationSpec =
                spring(
                  dampingRatio = 0.82f,
                  stiffness = Spring.StiffnessMediumLow,
                ),
              initialOffsetY = { (it * 0.88f).roundToInt() },
            ) +
              scaleIn(
                initialScale = 0.82f,
                transformOrigin = TransformOrigin(0.5f, 0.88f),
                animationSpec = tween(260, easing = FastOutSlowInEasing),
              ) +
              fadeIn(animationSpec = tween(180)),
          exit =
            slideOutVertically(
              animationSpec = tween(240, easing = FastOutSlowInEasing),
              targetOffsetY = { (it * 0.88f).roundToInt() },
            ) +
              scaleOut(
                targetScale = 0.82f,
                transformOrigin = TransformOrigin(0.5f, 0.88f),
                animationSpec = tween(220, easing = FastOutSlowInEasing),
              ) +
              fadeOut(animationSpec = tween(170)),
        ) {
          val panelH = cardH * (if (isHomeSettings) 0.750f else 0.690f)
          val panelW = panelH * (935f / 1253f)
          val panelCloseSize = panelW * 0.148f
          val panelCloseTouchSize = maxOf(48.dp, panelCloseSize)
          val panelInteraction = remember { MutableInteractionSource() }
          val panelCloseInteraction = remember { MutableInteractionSource() }
          val panelClosePressed by panelCloseInteraction.collectIsPressedAsState()
          val panelCloseScale by animateFloatAsState(
            targetValue = if (panelClosePressed) 0.90f else 1f,
            animationSpec = spring(),
            label = "levels_panel_close_scale",
          )

          Box(
            modifier =
              Modifier
                .size(width = panelW, height = panelH)
                .clickable(
                  interactionSource = panelInteraction,
                  indication = null,
                  onClick = {},
                )
                .testTag("settings_levels_panel"),
          ) {
            Image(
              painter = painterResource(id = R.drawable.panel_settings_levels),
              contentDescription = "Theme Stages",
              contentScale = ContentScale.Fit,
              modifier = Modifier.fillMaxSize(),
            )

            // Red glossy 'X' button from sprite sheet positioned at upper-right corner matching video
            Box(
              modifier =
                Modifier
                  .align(Alignment.TopStart)
                  .offset(
                    x = panelW * 0.962f - panelCloseTouchSize * 0.5f,
                    y = panelH * 0.044f - panelCloseTouchSize * 0.5f,
                  )
                  .size(panelCloseTouchSize)
                  .scale(panelCloseScale)
                  .clickable(
                    interactionSource = panelCloseInteraction,
                    indication = null,
                    onClick = { showLevelsPanel = false },
                  )
                  .semantics { contentDescription = "Close Stages Panel" }
                  .testTag("settings_levels_close_button"),
              contentAlignment = Alignment.Center,
            ) {
              Image(
                painter = painterResource(id = R.drawable.btn_panel_close_red),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(panelCloseSize),
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun SettingsSpriteButton(
  drawableRes: Int,
  contentDescription: String,
  onClick: () -> Unit,
  testTag: String,
  modifier: Modifier = Modifier,
) {
  val interaction = remember { MutableInteractionSource() }
  val pressed by interaction.collectIsPressedAsState()
  val scale by animateFloatAsState(if (pressed) 0.92f else 1f, spring(), label = "${testTag}_scale")

  Box(
    modifier =
      modifier
        .scale(scale)
        .clickable(
          interactionSource = interaction,
          indication = null,
          onClick = onClick,
        )
        .testTag(testTag),
    contentAlignment = Alignment.Center,
  ) {
    Image(
      painter = painterResource(id = drawableRes),
      contentDescription = contentDescription,
      contentScale = ContentScale.Fit,
      modifier = Modifier.fillMaxSize(),
    )
  }
}

@Composable
fun SettingToggleRow(
  label: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
) {
  Row(
    modifier =
      Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(Color(0xFFF1F6FC))
        .padding(horizontal = 14.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Text(text = label, style = MaterialTheme.typography.bodyLarge, color = OutlineInk)
    Switch(checked = checked, onCheckedChange = onCheckedChange)
  }
}

@Composable
fun HelpDialog(onDismiss: () -> Unit) {
  CartoonPopupCard(title = "HOW TO PLAY", headerColor = HudBlue, onDismiss = onDismiss) {
    HelpStepRow(
      step = "1",
      title = "Tap Clear Buses",
      desc = "Vehicles drive in the direction of their roof arrow. Tap unblocked vehicles to send them to the waiting bay.",
    )
    Spacer(Modifier.height(10.dp))
    HelpStepRow(
      step = "2",
      title = "Board Matching Passengers",
      desc = "Stickmen ride the top conveyor belt and jump into waiting buses of the same color.",
    )
    Spacer(Modifier.height(10.dp))
    HelpStepRow(
      step = "3",
      title = "Watch Slots & Tunnels",
      desc = "Don't fill all waiting slots with mismatched buses! Tunnels spawn extra vehicles once space opens.",
    )
    Spacer(Modifier.height(18.dp))
    GlossyButton(
      text = "Got It!",
      topColor = PlayGreen,
      bottomColor = PlayGreenDark,
      onClick = onDismiss,
      modifier = Modifier.fillMaxWidth(),
      testTag = "help_close_button",
    )
  }
}

@Composable
private fun HelpStepRow(step: String, title: String, desc: String) {
  Row(
    modifier =
      Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(Color(0xFFF3F8FF))
        .padding(10.dp),
    verticalAlignment = Alignment.Top,
  ) {
    Box(
      modifier = Modifier.size(26.dp).clip(CircleShape).background(HudBlue),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = step,
        style = MaterialTheme.typography.labelLarge.copy(fontFamily = FredokaFontFamily),
        color = Color.White,
      )
    }
    Spacer(Modifier.width(10.dp))
    Column {
      Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp), color = OutlineInk)
      Text(text = desc, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF4A5870))
    }
  }
}

@Composable
fun BoosterPurchaseDialog(
  booster: BoosterType,
  coins: Int,
  notice: String?,
  onGetFree: () -> Unit,
  onBuy: () -> Unit,
  onDismiss: () -> Unit,
) {
  val drawableRes =
    when (booster) {
      BoosterType.BOARD -> R.drawable.popup_booster_board
      BoosterType.REFRESH -> R.drawable.popup_booster_refresh
      BoosterType.VIP_SLOT -> R.drawable.popup_booster_vip
    }
  val aspectRatio =
    when (booster) {
      BoosterType.BOARD -> 894f / 1124f
      BoosterType.REFRESH -> 890f / 1122f
      BoosterType.VIP_SLOT -> 890f / 1143f
    }
  val closeCenterY = if (booster == BoosterType.VIP_SLOT) 0.161f else 0.148f
  val buttonsTopY = if (booster == BoosterType.VIP_SLOT) 0.775f else 0.790f

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Column(
      modifier = Modifier.widthIn(max = 350.dp).fillMaxWidth(0.86f),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      BoxWithConstraints(
        modifier =
          Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .testTag("booster_popup_card"),
      ) {
        val cardW = maxWidth
        val cardH = maxHeight

        Image(
          painter = painterResource(id = drawableRes),
          contentDescription = booster.title,
          contentScale = ContentScale.Fit,
          modifier = Modifier.fillMaxSize(),
        )

        // Top-right red 'X' close button
        val closeSize = maxOf(48.dp, cardW * 0.145f)
        Box(
          modifier =
            Modifier
              .offset(
                x = cardW * 0.932f - closeSize / 2f,
                y = cardH * closeCenterY - closeSize / 2f,
              )
              .size(closeSize)
              .clip(CircleShape)
              .clickable(onClick = onDismiss)
              .semantics { contentDescription = "Close ${booster.title} popup" }
              .testTag("booster_popup_close"),
        )

        // Bottom-left 'Get' free booster button
        Box(
          modifier =
            Modifier
              .offset(x = cardW * 0.105f, y = cardH * buttonsTopY)
              .size(width = cardW * 0.375f, height = maxOf(48.dp, cardH * 0.155f))
              .clip(RoundedCornerShape(16.dp))
              .clickable(onClick = onGetFree)
              .semantics { contentDescription = "Get ${booster.title}" }
              .testTag("booster_popup_get"),
        )

        // Bottom-right '200' coin purchase button
        Box(
          modifier =
            Modifier
              .offset(x = cardW * 0.505f, y = cardH * buttonsTopY)
              .size(width = cardW * 0.385f, height = maxOf(48.dp, cardH * 0.155f))
              .clip(RoundedCornerShape(16.dp))
              .clickable(onClick = onBuy)
              .semantics { contentDescription = "Buy ${booster.title} for ${booster.costCoins} coins" }
              .testTag("buy_booster_confirm"),
        )
      }

      if (notice != null) {
        Spacer(Modifier.height(10.dp))
        Box(
          modifier =
            Modifier
              .clip(RoundedCornerShape(14.dp))
              .background(Color.White.copy(alpha = 0.96f))
              .border(2.dp, AlertRed, RoundedCornerShape(14.dp))
              .padding(horizontal = 14.dp, vertical = 6.dp),
        ) {
          Text(
            text = "$notice (Coins: $coins)",
            style = MaterialTheme.typography.labelLarge.copy(fontFamily = FredokaFontFamily),
            color = AlertRed,
            textAlign = TextAlign.Center,
          )
        }
      }
    }
  }
}

@Composable
fun UnlockSlotDialog(
  onConfirm: () -> Unit,
  onDismiss: () -> Unit,
) {
  val cardPopScale = remember { Animatable(0.80f) }
  LaunchedEffect(Unit) {
    cardPopScale.animateTo(
      targetValue = 1f,
      animationSpec =
        spring(
          dampingRatio = Spring.DampingRatioMediumBouncy,
          stiffness = Spring.StiffnessMedium,
        ),
    )
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    val cardAspect = 406f / 508f
    BoxWithConstraints(
      modifier =
        Modifier
          .widthIn(max = 360.dp)
          .fillMaxWidth(0.86f)
          .aspectRatio(cardAspect)
          .scale(cardPopScale.value)
          .testTag("unlock_slot_popup_card"),
    ) {
      val cardW = maxWidth
      val cardH = maxHeight

      // 1. Red "Parking Slot — Add one Parking Slot" panel artwork
      Image(
        painter = painterResource(id = R.drawable.popup_parking_slot),
        contentDescription = "Add one Parking Slot",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize(),
      )

      // 2. Top-right red close (X) button aligned on upper-right ribbon tip like reference image
      val closeBtnSize = cardW * 0.112f
      val closeTouchSize = maxOf(closeBtnSize, 48.dp)
      val closeInteraction = remember { MutableInteractionSource() }
      val closePressed by closeInteraction.collectIsPressedAsState()
      val closeScale by animateFloatAsState(
        if (closePressed) 0.90f else 1f,
        spring(),
        label = "unlock_slot_close_scale",
      )
      Box(
        modifier =
          Modifier
            .align(Alignment.TopStart)
            .offset(
              x = cardW * 0.946f - closeTouchSize * 0.5f,
              y = cardH * 0.108f - closeTouchSize * 0.5f,
            )
            .size(closeTouchSize)
            .scale(closeScale)
            .clickable(
              interactionSource = closeInteraction,
              indication = null,
              onClick = onDismiss,
            )
            .testTag("unlock_slot_close_button"),
        contentAlignment = Alignment.Center,
      ) {
        Image(
          painter = painterResource(id = R.drawable.btn_panel_close_red),
          contentDescription = "Close Parking Slot popup",
          contentScale = ContentScale.Fit,
          modifier = Modifier.size(closeBtnSize),
        )
      }

      // 3. Green glossy "Get" button from sprite sheet centered in lower panel like reference image
      val getW = cardW * 0.435f
      val getH = getW * (250f / 656f)
      val getTouchH = maxOf(getH, 48.dp)
      val getInteraction = remember { MutableInteractionSource() }
      val getPressed by getInteraction.collectIsPressedAsState()
      val getScale by animateFloatAsState(
        if (getPressed) 0.92f else 1f,
        spring(),
        label = "unlock_slot_get_scale",
      )
      Box(
        modifier =
          Modifier
            .align(Alignment.TopCenter)
            .offset(y = cardH * 0.874f - getTouchH * 0.5f)
            .size(width = getW, height = getTouchH)
            .scale(getScale)
            .clickable(
              interactionSource = getInteraction,
              indication = null,
              onClick = onConfirm,
            )
            .testTag("confirm_unlock_slot"),
        contentAlignment = Alignment.Center,
      ) {
        Image(
          painter = painterResource(id = R.drawable.btn_parking_slot_get),
          contentDescription = "Get extra Parking Slot",
          contentScale = ContentScale.Fit,
          modifier = Modifier.size(width = getW, height = getH),
        )
      }
    }
  }
}

@Composable
fun ShopDialog(
  @Suppress("UNUSED_PARAMETER") coins: Int,
  @Suppress("UNUSED_PARAMETER") boardCount: Int,
  @Suppress("UNUSED_PARAMETER") refreshCount: Int,
  @Suppress("UNUSED_PARAMETER") vipCount: Int,
  @Suppress("UNUSED_PARAMETER") notice: String?,
  @Suppress("UNUSED_PARAMETER") onBuyBooster: (BoosterType) -> Unit,
  @Suppress("UNUSED_PARAMETER") onClaimFreeCoins: () -> Unit,
  onDismiss: () -> Unit,
) {
  BackHandler(onBack = onDismiss)
  val backdropInteraction = remember { MutableInteractionSource() }

  Box(
    modifier =
      Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            colors = listOf(Color(0xFF682BD0), Color(0xFF5620B8), Color(0xFF47189E)),
          ),
        )
        .clickable(
          interactionSource = backdropInteraction,
          indication = null,
          onClick = onDismiss,
        )
        .testTag("shop_screen_overlay"),
  ) {
    Image(
      painter = painterResource(id = R.drawable.bg_shop_top_banner),
      contentDescription = stringResource(R.string.shop_top_banner_desc),
      contentScale = ContentScale.FillWidth,
      modifier =
        Modifier
          .align(Alignment.TopCenter)
          .windowInsetsPadding(WindowInsets.statusBars)
          .fillMaxWidth()
          .aspectRatio(856f / 131f)
          .testTag("shop_top_banner"),
    )
  }
}

@Composable
fun SettingsDialog(
  soundEnabled: Boolean,
  vibrationEnabled: Boolean,
  cloudSaveEnabled: Boolean,
  languageLabel: String,
  onToggleSound: (Boolean) -> Unit,
  onToggleVibration: (Boolean) -> Unit,
  onToggleCloudSave: (Boolean) -> Unit,
  onCycleLanguage: () -> Unit,
  onOpenSupport: () -> Unit,
  onResetToLevel1: () -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
) {
  SettingsPopupOverlay(
    soundEnabled = soundEnabled,
    vibrationEnabled = vibrationEnabled,
    cloudSaveEnabled = cloudSaveEnabled,
    languageLabel = languageLabel,
    onToggleSound = onToggleSound,
    onToggleVibration = onToggleVibration,
    onToggleCloudSave = onToggleCloudSave,
    onCycleLanguage = onCycleLanguage,
    onOpenSupport = onOpenSupport,
    onDismiss = onDismiss,
    onRestart = onResetToLevel1,
    onHome = null,
    modifier = modifier,
  )
}

@Composable
fun StarterPackDialog(
  alreadyClaimed: Boolean,
  onActivate: () -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val backdropInteraction = remember { MutableInteractionSource() }
  val cardInteraction = remember { MutableInteractionSource() }
  val cardPopScale = remember { Animatable(0.76f) }

  val infiniteTransition = rememberInfiniteTransition(label = "starter_pack_dialog_anim")
  val raySpin by
    infiniteTransition.animateFloat(
      initialValue = 0f,
      targetValue = 360f,
      animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Restart),
      label = "starter_pack_dialog_rays",
    )
  val pulsePhase by
    infiniteTransition.animateFloat(
      initialValue = 0f,
      targetValue = 1f,
      animationSpec = infiniteRepeatable(tween(2400, easing = LinearEasing), RepeatMode.Restart),
      label = "starter_pack_dialog_pulse",
    )

  LaunchedEffect(Unit) {
    cardPopScale.animateTo(
      targetValue = 1f,
      animationSpec =
        spring(
          dampingRatio = Spring.DampingRatioMediumBouncy,
          stiffness = Spring.StiffnessMedium,
        ),
    )
  }

  BoxWithConstraints(
    modifier =
      modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.74f))
        .clickable(
          interactionSource = backdropInteraction,
          indication = null,
          onClick = onDismiss,
        )
        .windowInsetsPadding(WindowInsets.statusBars)
        .windowInsetsPadding(WindowInsets.displayCutout)
        .windowInsetsPadding(WindowInsets.navigationBars)
        .padding(horizontal = 6.dp, vertical = 10.dp)
        .testTag("starter_pack_dim_backdrop"),
    contentAlignment = Alignment.Center,
  ) {
    val cardAspect = 982f / 1336f
    val targetCardW = minOf(maxWidth * 0.95f, 470.dp, (maxHeight * 0.94f) * cardAspect)
    BoxWithConstraints(
      modifier =
        Modifier
          .width(targetCardW)
          .aspectRatio(cardAspect)
          .scale(cardPopScale.value)
          .clickable(
            interactionSource = cardInteraction,
            indication = null,
            onClick = {},
          )
          .testTag("starter_pack_popup_card"),
    ) {
      val cardW = maxWidth
      val cardH = maxHeight

      // 1. New clean Sky-Blue Starter Pack base card
      Image(
        painter = painterResource(id = R.drawable.popup_starter_pack),
        contentDescription = "Starter Pack",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize(),
      )

      // 2. Behind-the-chest animated glow halo, rotating sunbeam rays & twinkling sparkles
      val chestW = cardW * 0.67f
      val chestH = chestW * (303f / 362f)
      val chestCenterY = cardH * 0.376f
      val breathScale = 1f + 0.025f * sin(pulsePhase * 2f * PI.toFloat())

      // Clipped strictly inside the inner sky-blue upper panel so rays stay behind chest & inside frame
      Box(
        modifier =
          Modifier
            .align(Alignment.TopCenter)
            .offset(y = cardH * 0.154f)
            .size(width = cardW * 0.85f, height = cardH * 0.44f)
            .clip(RoundedCornerShape(cardW * 0.04f)),
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val cx = size.width * 0.5f
          // Align glow/ray center with the chest center (0.376 - 0.154) / 0.44 ≈ 0.505
          val cy = size.height * 0.505f
          val maxR = size.maxDimension * 0.62f
          val haloAlpha = 0.74f + 0.22f * sin(pulsePhase * 2f * PI.toFloat())

          // Soft radiant golden-white halo behind the treasure chest
          drawCircle(
            brush =
              Brush.radialGradient(
                colors =
                  listOf(
                    Color(0xF0FFF8D6).copy(alpha = haloAlpha),
                    Color(0x99FFE082).copy(alpha = haloAlpha * 0.72f),
                    Color(0x33FFFFFF).copy(alpha = haloAlpha * 0.45f),
                    Color.Transparent,
                  ),
                center = Offset(cx, cy),
                radius = maxR * 0.78f,
              ),
            radius = maxR * 0.78f,
            center = Offset(cx, cy),
          )

          // Rotating radial sunbeams emerging from behind the chest
          val rayBrush =
            Brush.radialGradient(
              colors =
                listOf(
                  Color(0xC8FFFFFF),
                  Color(0x66FFF4CC),
                  Color(0x18FFFFFF),
                  Color.Transparent,
                ),
              center = Offset(cx, cy),
              radius = maxR,
            )
          rotate(degrees = raySpin, pivot = Offset(cx, cy)) {
            val rayCount = 12
            for (i in 0 until rayCount) {
              val a0 = (i * (360.0 / rayCount)) * (PI / 180.0)
              val halfSpread = 0.115
              val a1 = a0 + halfSpread
              val a2 = a0 - halfSpread
              val rayPath =
                Path().apply {
                  moveTo(cx, cy)
                  lineTo(cx + cos(a1).toFloat() * maxR, cy + sin(a1).toFloat() * maxR)
                  lineTo(cx + cos(a2).toFloat() * maxR, cy + sin(a2).toFloat() * maxR)
                  close()
                }
              drawPath(rayPath, brush = rayBrush)
            }
          }

          // Twinkling 4-point sparkles around the chest matching reference positions
          val sparkleDefs =
            listOf(
              Triple(0.08f, 0.34f, 0.0f),
              Triple(0.89f, 0.16f, 0.33f),
              Triple(0.92f, 0.62f, 0.67f),
            )
          for ((sxFrac, syFrac, phaseOffset) in sparkleDefs) {
            val twinkle = 0.45f + 0.55f * (0.5f + 0.5f * sin((pulsePhase + phaseOffset) * 2f * PI.toFloat()))
            val sx = size.width * sxFrac
            val sy = size.height * syFrac
            val sr = size.minDimension * 0.045f * (0.75f + 0.35f * twinkle)
            val starPath =
              Path().apply {
                moveTo(sx, sy - sr)
                quadraticTo(sx, sy, sx + sr, sy)
                quadraticTo(sx, sy, sx, sy + sr)
                quadraticTo(sx, sy, sx - sr, sy)
                quadraticTo(sx, sy, sx, sy - sr)
                close()
              }
            drawPath(starPath, color = Color.White.copy(alpha = 0.88f * twinkle))
          }
        }
      }

      // 3. Row 2 Col 3 x1000 treasure chest positioned identically to the second reference image
      Image(
        painter = painterResource(id = R.drawable.img_starter_pack_chest),
        contentDescription = "1000 Coins Chest",
        contentScale = ContentScale.Fit,
        modifier =
          Modifier
            .align(Alignment.TopCenter)
            .offset(y = chestCenterY - chestH * 0.5f)
            .size(width = chestW, height = chestH)
            .scale(breathScale)
            .testTag("starter_pack_chest"),
      )

      // 4. Top-right close (X) button positioned on upper-right ribbon corner
      val closeBtnSize = cardW * 0.112f
      val closeTouchSize = maxOf(closeBtnSize, 48.dp)
      val closeInteraction = remember { MutableInteractionSource() }
      val closePressed by closeInteraction.collectIsPressedAsState()
      val closeScale by animateFloatAsState(if (closePressed) 0.90f else 1f, spring(), label = "starter_pack_close_scale")
      Box(
        modifier =
          Modifier
            .align(Alignment.TopStart)
            .offset(
              x = cardW * 0.916f - closeTouchSize * 0.5f,
              y = cardH * 0.132f - closeTouchSize * 0.5f,
            )
            .size(closeTouchSize)
            .scale(closeScale)
            .clickable(
              interactionSource = closeInteraction,
              indication = null,
              onClick = onDismiss,
            )
            .testTag("starter_pack_close_button"),
        contentAlignment = Alignment.Center,
      ) {
        Image(
          painter = painterResource(id = R.drawable.btn_settings_close),
          contentDescription = "Close",
          contentScale = ContentScale.Fit,
          modifier = Modifier.size(closeBtnSize),
        )
      }

      // 5. Middle boosters box (VIP x2, Bus x2, Refresh x2) sized & centered cleanly below chest
      val rewardW = cardW * 0.50f
      val rewardH = rewardW * (289f / 777f)
      Image(
        painter = painterResource(id = R.drawable.panel_starter_pack_rewards),
        contentDescription = "2 VIP, 2 Bus Clear, 2 Refresh",
        contentScale = ContentScale.Fit,
        modifier =
          Modifier
            .align(Alignment.TopCenter)
            .offset(y = cardH * 0.602f)
            .size(width = rewardW, height = rewardH)
            .testTag("starter_pack_rewards_panel"),
      )

      // 6. Bottom "Rs 500.00" + "50%OFF" price button centered cleanly in lower panel
      val priceW = cardW * 0.48f
      val priceH = priceW * (305f / 789f)
      val priceInteraction = remember { MutableInteractionSource() }
      val pricePressed by priceInteraction.collectIsPressedAsState()
      val priceScale by animateFloatAsState(if (pricePressed) 0.93f else 1f, spring(), label = "starter_pack_price_scale")

      Box(
        modifier =
          Modifier
            .align(Alignment.TopCenter)
            .offset(y = cardH * 0.776f)
            .size(width = priceW, height = maxOf(priceH, 48.dp))
            .scale(priceScale)
            .clickable(
              interactionSource = priceInteraction,
              indication = null,
              onClick = {
                if (!alreadyClaimed) {
                  onActivate()
                } else {
                  onDismiss()
                }
              },
            )
            .testTag("starter_pack_buy_button"),
        contentAlignment = Alignment.Center,
      ) {
        Image(
          painter = painterResource(id = R.drawable.btn_starter_pack_price),
          contentDescription = if (alreadyClaimed) "Starter Pack Claimed" else "Buy Starter Pack for Rs 500.00",
          contentScale = ContentScale.Fit,
          modifier = Modifier.size(width = priceW, height = priceH),
        )
      }
    }
  }
}

@Composable
fun NoAdsPackDialog(
  alreadyUnlocked: Boolean,
  onActivate: () -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val backdropInteraction = remember { MutableInteractionSource() }
  val cardInteraction = remember { MutableInteractionSource() }
  val cardPopScale = remember { Animatable(0.76f) }

  val infiniteTransition = rememberInfiniteTransition(label = "no_ads_dialog_anim")
  val raySpin by
    infiniteTransition.animateFloat(
      initialValue = 0f,
      targetValue = 360f,
      animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Restart),
      label = "no_ads_dialog_rays",
    )
  val pulsePhase by
    infiniteTransition.animateFloat(
      initialValue = 0f,
      targetValue = 1f,
      animationSpec = infiniteRepeatable(tween(2400, easing = LinearEasing), RepeatMode.Restart),
      label = "no_ads_dialog_pulse",
    )

  LaunchedEffect(Unit) {
    cardPopScale.animateTo(
      targetValue = 1f,
      animationSpec =
        spring(
          dampingRatio = Spring.DampingRatioMediumBouncy,
          stiffness = Spring.StiffnessMedium,
        ),
    )
  }

  BoxWithConstraints(
    modifier =
      modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.74f))
        .clickable(
          interactionSource = backdropInteraction,
          indication = null,
          onClick = onDismiss,
        )
        .windowInsetsPadding(WindowInsets.statusBars)
        .windowInsetsPadding(WindowInsets.displayCutout)
        .windowInsetsPadding(WindowInsets.navigationBars)
        .padding(horizontal = 6.dp, vertical = 10.dp)
        .testTag("no_ads_dim_backdrop"),
    contentAlignment = Alignment.Center,
  ) {
    val cardAspect = 1024f / 1536f
    val targetCardW = minOf(maxWidth * 0.98f, 480.dp, (maxHeight * 0.96f) * cardAspect)
    BoxWithConstraints(
      modifier =
        Modifier
          .width(targetCardW)
          .aspectRatio(cardAspect)
          .scale(cardPopScale.value)
          .clickable(
            interactionSource = cardInteraction,
            indication = null,
            onClick = {},
          )
          .testTag("no_ads_popup_card"),
    ) {
      val cardW = maxWidth
      val cardH = maxHeight

      // 1. Base green No Ads Pack card with ribbon
      Image(
        painter = painterResource(id = R.drawable.popup_no_ads_pack),
        contentDescription = "No Ads Pack",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize(),
      )

      // 2. Top-right red close (X) button from sprite sheet positioned identically to reference image
      val closeBtnSize = cardW * 0.106f
      val closeTouchSize = maxOf(closeBtnSize, 48.dp)
      val closeInteraction = remember { MutableInteractionSource() }
      val closePressed by closeInteraction.collectIsPressedAsState()
      val closeScale by animateFloatAsState(if (closePressed) 0.90f else 1f, spring(), label = "no_ads_close_scale")
      Box(
        modifier =
          Modifier
            .align(Alignment.TopStart)
            .offset(
              x = cardW * 0.935f - closeTouchSize * 0.5f,
              y = cardH * 0.098f - closeTouchSize * 0.5f,
            )
            .size(closeTouchSize)
            .scale(closeScale)
            .clickable(
              interactionSource = closeInteraction,
              indication = null,
              onClick = onDismiss,
            )
            .testTag("no_ads_close_button"),
        contentAlignment = Alignment.Center,
      ) {
        Image(
          painter = painterResource(id = R.drawable.btn_panel_close_red),
          contentDescription = "Close",
          contentScale = ContentScale.Fit,
          modifier = Modifier.size(closeBtnSize),
        )
      }

      // 3. Upper-center Orange ADS badge with animated rotating sunbeams & glow BEHIND it
      val badgeW = cardW * 0.305f
      val badgeH = badgeW * (340f / 354f)
      val glowBoxSize = cardW * 0.64f
      val badgeCenterY = cardH * 0.324f
      val breathScale = 1f + 0.032f * sin(pulsePhase * 2f * PI.toFloat())

      Box(
        modifier =
          Modifier
            .align(Alignment.TopCenter)
            .offset(y = badgeCenterY - glowBoxSize * 0.5f)
            .size(glowBoxSize),
        contentAlignment = Alignment.Center,
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val cx = size.width * 0.5f
          val cy = size.height * 0.5f
          val maxR = size.minDimension * 0.5f
          val haloAlpha = 0.72f + 0.24f * sin(pulsePhase * 2f * PI.toFloat())

          // Soft warm radiant core glow behind the ADS emblem
          drawCircle(
            brush =
              Brush.radialGradient(
                colors =
                  listOf(
                    Color(0xF0FFF8E6).copy(alpha = haloAlpha),
                    Color(0x99FFF5B8).copy(alpha = haloAlpha * 0.75f),
                    Color.Transparent,
                  ),
                center = Offset(cx, cy),
                radius = maxR * 0.85f,
              ),
            radius = maxR * 0.85f,
            center = Offset(cx, cy),
          )

          // Rotating radial sunbeams emerging from behind the ADS emblem
          val rayBrush =
            Brush.radialGradient(
              colors =
                listOf(
                  Color(0xD8FFFFFF),
                  Color(0x70FFF4FF),
                  Color(0x18FFFFFF),
                  Color.Transparent,
                ),
              center = Offset(cx, cy),
              radius = maxR,
            )
          rotate(degrees = raySpin, pivot = Offset(cx, cy)) {
            val rayCount = 12
            for (i in 0 until rayCount) {
              val a0 = (i * (360.0 / rayCount)) * (PI / 180.0)
              val halfSpread = 0.12
              val a1 = a0 + halfSpread
              val a2 = a0 - halfSpread
              val rayPath =
                Path().apply {
                  moveTo(cx, cy)
                  lineTo(cx + cos(a1).toFloat() * maxR, cy + sin(a1).toFloat() * maxR)
                  lineTo(cx + cos(a2).toFloat() * maxR, cy + sin(a2).toFloat() * maxR)
                  close()
                }
              drawPath(rayPath, brush = rayBrush)
            }
          }
        }

        Image(
          painter = painterResource(id = R.drawable.badge_no_ads_orange),
          contentDescription = "No Ads",
          contentScale = ContentScale.Fit,
          modifier =
            Modifier
              .size(width = badgeW, height = badgeH)
              .scale(breathScale),
        )
      }

      // 4. Green VIP reward bar centered between ADS badge and "Remove ads permanently"
      val rewardW = cardW * 0.72f
      val rewardH = rewardW * (533f / 1823f)
      Image(
        painter = painterResource(id = R.drawable.panel_no_ads_rewards),
        contentDescription = "1 VIP, 1 Bus Clear, 1 Refresh, 1000 Coins",
        contentScale = ContentScale.Fit,
        modifier =
          Modifier
            .align(Alignment.TopCenter)
            .offset(y = cardH * 0.490f)
            .size(width = rewardW, height = rewardH)
            .testTag("no_ads_rewards_panel"),
      )

      // 4b. Outlined "Remove ads permanently" subtitle centered between rewards panel & price button
      val subtitleText = stringResource(id = R.string.no_ads_remove_permanently)
      val subtitleSize = (cardW.value * 0.049f).coerceIn(14f, 21f).sp
      val subtitleStyle =
        MaterialTheme.typography.titleMedium.copy(
          fontFamily = FredokaFontFamily,
          fontSize = subtitleSize,
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 0.2.sp,
        )
      Box(
        modifier =
          Modifier
            .align(Alignment.TopCenter)
            .offset(y = cardH * 0.720f)
            .fillMaxWidth(0.86f),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = subtitleText,
          style =
            subtitleStyle.copy(
              drawStyle = Stroke(width = 8f, join = StrokeJoin.Round),
            ),
          color = Color(0xFF0A4D18),
          textAlign = TextAlign.Center,
          maxLines = 1,
        )
        Text(
          text = subtitleText,
          style = subtitleStyle,
          color = Color.White,
          textAlign = TextAlign.Center,
          maxLines = 1,
        )
      }

      // 5. Glossy dark-purple button (Row 3 Col 2 from sprite sheet) with bold outlined "Rs 3,000.00" text
      val priceW = cardW * 0.485f
      val priceH = priceW * (294f / 690f)
      val priceInteraction = remember { MutableInteractionSource() }
      val pricePressed by priceInteraction.collectIsPressedAsState()
      val priceScale by animateFloatAsState(if (pricePressed) 0.93f else 1f, spring(), label = "no_ads_price_scale")
      val priceLabel =
        stringResource(
          id = if (alreadyUnlocked) R.string.no_ads_active_label else R.string.no_ads_price_label,
        )
      val priceTextStyle =
        MaterialTheme.typography.titleLarge.copy(
          fontFamily = LilitaOneFontFamily,
          fontSize = (priceH.value * 0.37f).coerceIn(17f, 25f).sp,
          fontWeight = FontWeight.Normal,
          letterSpacing = 0.3.sp,
        )

      Box(
        modifier =
          Modifier
            .align(Alignment.TopCenter)
            .offset(y = cardH * 0.808f)
            .size(width = priceW, height = maxOf(priceH, 48.dp))
            .scale(priceScale)
            .clickable(
              interactionSource = priceInteraction,
              indication = null,
              onClick = {
                if (!alreadyUnlocked) {
                  onActivate()
                } else {
                  onDismiss()
                }
              },
            )
            .testTag("no_ads_buy_button"),
        contentAlignment = Alignment.Center,
      ) {
        Box(
          modifier = Modifier.size(width = priceW, height = priceH),
          contentAlignment = Alignment.Center,
        ) {
          Image(
            painter = painterResource(id = R.drawable.btn_no_ads_price),
            contentDescription = if (alreadyUnlocked) "No Ads Pack Active" else "Buy No Ads Pack for Rs 3,000.00",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(),
          )
          // Subtle dark drop shadow
          Text(
            text = priceLabel,
            style =
              priceTextStyle.copy(
                drawStyle = Stroke(width = 9.5f, join = StrokeJoin.Round),
              ),
            color = Color(0xFF190233),
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = priceW * 0.08f).offset(y = 1.5.dp),
          )
          // Crisp dark-purple outline
          Text(
            text = priceLabel,
            style =
              priceTextStyle.copy(
                drawStyle = Stroke(width = 8.5f, join = StrokeJoin.Round),
              ),
            color = Color(0xFF28054E),
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = priceW * 0.08f),
          )
          // Bright white foreground text
          Text(
            text = priceLabel,
            style = priceTextStyle,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = priceW * 0.08f),
          )
        }
      }
    }
  }
}
