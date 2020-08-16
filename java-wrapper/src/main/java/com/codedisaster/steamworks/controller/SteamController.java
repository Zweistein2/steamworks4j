package com.codedisaster.steamworks.controller;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamInterface;

/**
 * Steam Controller support API.
 *
 * This API has been deprecated in favor of ISteamInput
 * - please see ISteamInput Interface for info
 *
 * @deprecated
 */
@Deprecated(since = "2.0.0")
public class SteamController extends SteamInterface {

	public enum Pad {
		Left,
		Right
	}

	public enum Source {
		None,
		LeftTrackpad,
		RightTrackpad,
		Joystick,
		ABXY,
		Switch,
		LeftTrigger,
		RightTrigger,
		LeftBumper,
		RightBumper,
		Gyro,
		CenterTrackpad,
		RightJoystick,
		DPad,
		Key,
		Mouse,
		LeftGyro
	}

	public enum SourceMode {
		None,
		Dpad,
		Buttons,
		FourButtons,
		AbsoluteMouse,
		RelativeMouse,
		JoystickMove,
		JoystickMouse,
		JoystickCamera,
		ScrollWheel,
		Trigger,
		TouchMenu,
		MouseJoystick,
		MouseRegion,
		RadialMenu,
		SingleButton,
		Switches;

		private static final SourceMode[] values = values();

		static SourceMode byOrdinal(final int ordinal) {
			return values[ordinal];
		}
	}

	public enum ActionOrigin {
		None,
		A,
		B,
		X,
		Y,
		LeftBumper,
		RightBumper,
		LeftGrip,
		RightGrip,
		Start,
		Back,
		LeftPad_Touch,
		LeftPad_Swipe,
		LeftPad_Click,
		LeftPad_DPadNorth,
		LeftPad_DPadSouth,
		LeftPad_DPadWest,
		LeftPad_DPadEast,
		RightPad_Touch,
		RightPad_Swipe,
		RightPad_Click,
		RightPad_DPadNorth,
		RightPad_DPadSouth,
		RightPad_DPadWest,
		RightPad_DPadEast,
		LeftTrigger_Pull,
		LeftTrigger_Click,
		RightTrigger_Pull,
		RightTrigger_Click,
		LeftStick_Move,
		LeftStick_Click,
		LeftStick_DPadNorth,
		LeftStick_DPadSouth,
		LeftStick_DPadWest,
		LeftStick_DPadEast,
		Gyro_Move,
		Gyro_Pitch,
		Gyro_Yaw,
		Gyro_Roll,

		PS4_X,
		PS4_Circle,
		PS4_Triangle,
		PS4_Square,
		PS4_LeftBumper,
		PS4_RightBumper,
		PS4_Options,
		PS4_Share,
		PS4_LeftPad_Touch,
		PS4_LeftPad_Swipe,
		PS4_LeftPad_Click,
		PS4_LeftPad_DPadNorth,
		PS4_LeftPad_DPadSouth,
		PS4_LeftPad_DPadWest,
		PS4_LeftPad_DPadEast,
		PS4_RightPad_Touch,
		PS4_RightPad_Swipe,
		PS4_RightPad_Click,
		PS4_RightPad_DPadNorth,
		PS4_RightPad_DPadSouth,
		PS4_RightPad_DPadWest,
		PS4_RightPad_DPadEast,
		PS4_CenterPad_Touch,
		PS4_CenterPad_Swipe,
		PS4_CenterPad_Click,
		PS4_CenterPad_DPadNorth,
		PS4_CenterPad_DPadSouth,
		PS4_CenterPad_DPadWest,
		PS4_CenterPad_DPadEast,
		PS4_LeftTrigger_Pull,
		PS4_LeftTrigger_Click,
		PS4_RightTrigger_Pull,
		PS4_RightTrigger_Click,
		PS4_LeftStick_Move,
		PS4_LeftStick_Click,
		PS4_LeftStick_DPadNorth,
		PS4_LeftStick_DPadSouth,
		PS4_LeftStick_DPadWest,
		PS4_LeftStick_DPadEast,
		PS4_RightStick_Move,
		PS4_RightStick_Click,
		PS4_RightStick_DPadNorth,
		PS4_RightStick_DPadSouth,
		PS4_RightStick_DPadWest,
		PS4_RightStick_DPadEast,
		PS4_DPad_North,
		PS4_DPad_South,
		PS4_DPad_West,
		PS4_DPad_East,
		PS4_Gyro_Move,
		PS4_Gyro_Pitch,
		PS4_Gyro_Yaw,
		PS4_Gyro_Roll,

		XBoxOne_A,
		XBoxOne_B,
		XBoxOne_X,
		XBoxOne_Y,
		XBoxOne_LeftBumper,
		XBoxOne_RightBumper,
		XBoxOne_Menu,
		XBoxOne_View,
		XBoxOne_LeftTrigger_Pull,
		XBoxOne_LeftTrigger_Click,
		XBoxOne_RightTrigger_Pull,
		XBoxOne_RightTrigger_Click,
		XBoxOne_LeftStick_Move,
		XBoxOne_LeftStick_Click,
		XBoxOne_LeftStick_DPadNorth,
		XBoxOne_LeftStick_DPadSouth,
		XBoxOne_LeftStick_DPadWest,
		XBoxOne_LeftStick_DPadEast,
		XBoxOne_RightStick_Move,
		XBoxOne_RightStick_Click,
		XBoxOne_RightStick_DPadNorth,
		XBoxOne_RightStick_DPadSouth,
		XBoxOne_RightStick_DPadWest,
		XBoxOne_RightStick_DPadEast,
		XBoxOne_DPad_North,
		XBoxOne_DPad_South,
		XBoxOne_DPad_West,
		XBoxOne_DPad_East,

		XBox360_A,
		XBox360_B,
		XBox360_X,
		XBox360_Y,
		XBox360_LeftBumper,
		XBox360_RightBumper,
		XBox360_Start,
		XBox360_Back,
		XBox360_LeftTrigger_Pull,
		XBox360_LeftTrigger_Click,
		XBox360_RightTrigger_Pull,
		XBox360_RightTrigger_Click,
		XBox360_LeftStick_Move,
		XBox360_LeftStick_Click,
		XBox360_LeftStick_DPadNorth,
		XBox360_LeftStick_DPadSouth,
		XBox360_LeftStick_DPadWest,
		XBox360_LeftStick_DPadEast,
		XBox360_RightStick_Move,
		XBox360_RightStick_Click,
		XBox360_RightStick_DPadNorth,
		XBox360_RightStick_DPadSouth,
		XBox360_RightStick_DPadWest,
		XBox360_RightStick_DPadEast,
		XBox360_DPad_North,
		XBox360_DPad_South,
		XBox360_DPad_West,
		XBox360_DPad_East,

		SteamV2_A,
		SteamV2_B,
		SteamV2_X,
		SteamV2_Y,
		SteamV2_LeftBumper,
		SteamV2_RightBumper,
		SteamV2_LeftGrip_Lower,
		SteamV2_LeftGrip_Upper,
		SteamV2_RightGrip_Lower,
		SteamV2_RightGrip_Upper,
		SteamV2_LeftBumper_Pressure,
		SteamV2_RightBumper_Pressure,
		SteamV2_LeftGrip_Pressure,
		SteamV2_RightGrip_Pressure,
		SteamV2_LeftGrip_Upper_Pressure,
		SteamV2_RightGrip_Upper_Pressure,
		SteamV2_Start,
		SteamV2_Back,
		SteamV2_LeftPad_Touch,
		SteamV2_LeftPad_Swipe,
		SteamV2_LeftPad_Click,
		SteamV2_LeftPad_Pressure,
		SteamV2_LeftPad_DPadNorth,
		SteamV2_LeftPad_DPadSouth,
		SteamV2_LeftPad_DPadWest,
		SteamV2_LeftPad_DPadEast,
		SteamV2_RightPad_Touch,
		SteamV2_RightPad_Swipe,
		SteamV2_RightPad_Click,
		SteamV2_RightPad_Pressure,
		SteamV2_RightPad_DPadNorth,
		SteamV2_RightPad_DPadSouth,
		SteamV2_RightPad_DPadWest,
		SteamV2_RightPad_DPadEast,
		SteamV2_LeftTrigger_Pull,
		SteamV2_LeftTrigger_Click,
		SteamV2_RightTrigger_Pull,
		SteamV2_RightTrigger_Click,
		SteamV2_LeftStick_Move,
		SteamV2_LeftStick_Click,
		SteamV2_LeftStick_DPadNorth,
		SteamV2_LeftStick_DPadSouth,
		SteamV2_LeftStick_DPadWest,
		SteamV2_LeftStick_DPadEast,
		SteamV2_Gyro_Move,
		SteamV2_Gyro_Pitch,
		SteamV2_Gyro_Yaw,
		SteamV2_Gyro_Roll,

		Switch_A,
		Switch_B,
		Switch_X,
		Switch_Y,
		Switch_LeftBumper,
		Switch_RightBumper,
		Switch_Plus,
		Switch_Minus,
		Switch_Capture,
		Switch_LeftTrigger_Pull,
		Switch_LeftTrigger_Click,
		Switch_RightTrigger_Pull,
		Switch_RightTrigger_Click,
		Switch_LeftStick_Move,
		Switch_LeftStick_Click,
		Switch_LeftStick_DPadNorth,
		Switch_LeftStick_DPadSouth,
		Switch_LeftStick_DPadWest,
		Switch_LeftStick_DPadEast,
		Switch_RightStick_Move,
		Switch_RightStick_Click,
		Switch_RightStick_DPadNorth,
		Switch_RightStick_DPadSouth,
		Switch_RightStick_DPadWest,
		Switch_RightStick_DPadEast,
		Switch_DPad_North,
		Switch_DPad_South,
		Switch_DPad_West,
		Switch_DPad_East,
		Switch_ProGyro_Move,
		Switch_ProGyro_Pitch,
		Switch_ProGyro_Yaw,
		Switch_ProGyro_Roll,
		Switch_RightGyro_Move,
		Switch_RightGyro_Pitch,
		Switch_RightGyro_Yaw,
		Switch_RightGyro_Roll,
		Switch_LeftGyro_Move,
		Switch_LeftGyro_Pitch,
		Switch_LeftGyro_Yaw,
		Switch_LeftGyro_Roll,
		Switch_LeftGrip_Lower,
		Switch_LeftGrip_Upper,
		Switch_RightGrip_Lower,
		Switch_RightGrip_Upper,

		PS4_DPad_Move,
		XBoxOne_DPad_Move,
		XBox360_DPad_Move,
		Switch_DPad_Move;

		private static final ActionOrigin[] values = values();

		static ActionOrigin byOrdinal(final int ordinal) {
			return values[ordinal];
		}
	}

	public enum XboxOrigin {
		A,
		B,
		X,
		Y,
		LeftBumper,
		RightBumper,
		Menu,
		View,
		LeftTrigger_Pull,
		LeftTrigger_Click,
		RightTrigger_Pull,
		RightTrigger_Click,
		LeftStick_Move,
		LeftStick_Click,
		LeftStick_DPadNorth,
		LeftStick_DPadSouth,
		LeftStick_DPadWest,
		LeftStick_DPadEast,
		RightStick_Move,
		RightStick_Click,
		RightStick_DPadNorth,
		RightStick_DPadSouth,
		RightStick_DPadWest,
		RightStick_DPadEast,
		DPad_North,
		DPad_South,
		DPad_West,
		DPad_East
	}

	public enum InputType {
		Unknown,
		SteamController,
		XBox360Controller,
		XBoxOneController,
		GenericGamepad,
		PS4Controller,
		AppleMFiController,
		AndroidController,
		SwitchJoyConPair,
		SwitchJoyConSingle,
		SwitchProController,
		MobileTouch,
		PS3Controller;

		private static final InputType[] values = values();

		static InputType byOrdinal(final int ordinal) {
			return values[ordinal];
		}
	}

	public enum LEDFlag {
		SetColor,
		RestoreUserDefault
	}

	public static final int STEAM_CONTROLLER_MAX_COUNT = 16;
	public static final int STEAM_CONTROLLER_MAX_ANALOG_ACTIONS = 16;
	public static final int STEAM_CONTROLLER_MAX_DIGITAL_ACTIONS = 128;
	public static final int STEAM_CONTROLLER_MAX_ORIGINS = 8;

	public static final long STEAM_CONTROLLER_HANDLE_ALL_CONTROLLERS = 0xffffffffffffffffL;

	public static final float STEAM_CONTROLLER_MIN_ANALOG_ACTION_DATA = -1.0f;
	public static final float STEAM_CONTROLLER_MAX_ANALOG_ACTION_DATA = 1.0f;

	private final long[] controllerHandles = new long[STEAM_CONTROLLER_MAX_COUNT];
	private final int[] actionOrigins = new int[STEAM_CONTROLLER_MAX_ORIGINS];

	public SteamController() {
		super(SteamAPI.getSteamControllerPointer());
	}

	public boolean initController() {
		return init(pointer);
	}

	public boolean shutdownController() {
		return shutdown(pointer);
	}

	public void runFrame() {
		runFrame(pointer);
	}

	public int getConnectedControllers(final SteamControllerHandle[] handlesOut) {
		if (handlesOut.length < STEAM_CONTROLLER_MAX_COUNT) {
			throw new IllegalArgumentException("Array size must be at least STEAM_CONTROLLER_MAX_COUNT");
		}

		final int count = getConnectedControllers(pointer, controllerHandles);

		for (int i = 0; i < count; i++) {
			handlesOut[i] = new SteamControllerHandle(controllerHandles[i]);
		}

		return count;
	}

	public boolean showBindingPanel(final SteamControllerHandle controller) {
		return showBindingPanel(pointer, controller.handle);
	}

	public SteamControllerActionSetHandle getActionSetHandle(final String actionSetName) {
		return new SteamControllerActionSetHandle(getActionSetHandle(pointer, actionSetName));
	}

	public void activateActionSet(final SteamControllerHandle controller, final SteamControllerActionSetHandle actionSet) {
		activateActionSet(pointer, controller.handle, actionSet.handle);
	}

	public SteamControllerActionSetHandle getCurrentActionSet(final SteamControllerHandle controller) {
		return new SteamControllerActionSetHandle(getCurrentActionSet(pointer, controller.handle));
	}

	public SteamControllerDigitalActionHandle getDigitalActionHandle(final String actionName) {
		return new SteamControllerDigitalActionHandle(getDigitalActionHandle(pointer, actionName));
	}

	public void getDigitalActionData(final SteamControllerHandle controller,
									 final SteamControllerDigitalActionHandle digitalAction,
									 final SteamControllerDigitalActionData digitalActionData) {

		getDigitalActionData(pointer, controller.handle, digitalAction.handle, digitalActionData);
	}

	public int getDigitalActionOrigins(final SteamControllerHandle controller,
									   final SteamControllerActionSetHandle actionSet,
									   final SteamControllerDigitalActionHandle digitalAction,
									   final ActionOrigin[] originsOut) {

		if (originsOut.length < STEAM_CONTROLLER_MAX_ORIGINS) {
			throw new IllegalArgumentException("Array size must be at least STEAM_CONTROLLER_MAX_ORIGINS");
		}

		final int count = getDigitalActionOrigins(pointer, controller.handle,
												  actionSet.handle, digitalAction.handle, actionOrigins);

		for (int i = 0; i < count; i++) {
			originsOut[i] = ActionOrigin.byOrdinal(actionOrigins[i]);
		}

		return count;
	}

	public SteamControllerAnalogActionHandle getAnalogActionHandle(final String actionName) {
		return new SteamControllerAnalogActionHandle(getAnalogActionHandle(pointer, actionName));
	}

	public void getAnalogActionData(final SteamControllerHandle controller,
									final SteamControllerAnalogActionHandle analogAction,
									final SteamControllerAnalogActionData analoglActionData) {

		getAnalogActionData(pointer, controller.handle, analogAction.handle, analoglActionData);
	}

	public int getAnalogActionOrigins(final SteamControllerHandle controller,
									  final SteamControllerActionSetHandle actionSet,
									  final SteamControllerAnalogActionHandle analogAction,
									  final ActionOrigin[] originsOut) {

		if (originsOut.length < STEAM_CONTROLLER_MAX_ORIGINS) {
			throw new IllegalArgumentException("Array size must be at least STEAM_CONTROLLER_MAX_ORIGINS");
		}

		final int count = getAnalogActionOrigins(pointer, controller.handle,
												 actionSet.handle, analogAction.handle, actionOrigins);

		for (int i = 0; i < count; i++) {
			originsOut[i] = ActionOrigin.byOrdinal(actionOrigins[i]);
		}

		return count;
	}

	public void stopAnalogActionMomentum(final SteamControllerHandle controller,
										 final SteamControllerAnalogActionHandle analogAction) {

		stopAnalogActionMomentum(pointer, controller.handle, analogAction.handle);
	}

	public void triggerHapticPulse(final SteamControllerHandle controller, final Pad targetPad, final int durationMicroSec) {
		triggerHapticPulse(pointer, controller.handle, targetPad.ordinal(), durationMicroSec);
	}

	public void triggerRepeatedHapticPulse(final SteamControllerHandle controller, final Pad targetPad,
										   final int durationMicroSec, final int offMicroSec, final int repeat, final int flags) {

		triggerRepeatedHapticPulse(pointer, controller.handle, targetPad.ordinal(),
				durationMicroSec, offMicroSec, repeat, flags);
	}

	public void triggerVibration(final SteamControllerHandle controller, final short leftSpeed, final short rightSpeed) {
		triggerVibration(pointer, controller.handle, leftSpeed, rightSpeed);
	}

	public void setLEDColor(final SteamControllerHandle controller, final int colorR, final int colorG, final int colorB, final LEDFlag flags) {
		setLEDColor(pointer, controller.handle, (byte) (colorR & 0xff),
				(byte) (colorG & 0xff), (byte) (colorB & 0xff), flags.ordinal());
	}

	public int getGamepadIndexForController(final SteamControllerHandle controller) {
		return getGamepadIndexForController(pointer, controller.handle);
	}

	public SteamControllerHandle getControllerForGamepadIndex(final int index) {
		return new SteamControllerHandle(getControllerForGamepadIndex(pointer, index));
	}

	public void getMotionData(final SteamControllerHandle controller, final SteamControllerMotionData motionData) {
		getMotionData(pointer, controller.handle, motionData.data);
	}

	public String getStringForActionOrigin(final ActionOrigin origin) {
		return getStringForActionOrigin(pointer, origin.ordinal());
	}

	public String getGlyphForActionOrigin(final ActionOrigin origin) {
		return getGlyphForActionOrigin(pointer, origin.ordinal());
	}

	public InputType getInputTypeForHandle(final SteamControllerHandle controller) {
		return InputType.byOrdinal(getInputTypeForHandle(pointer, controller.handle));
	}

	// @off

	/*JNI
		#include "isteamcontroller.h"
	*/

	private static native boolean init(long pointer); /*
		ISteamController* controller = (ISteamController*) pointer;
		return controller->Init();
	*/

	private static native boolean shutdown(long pointer); /*
		ISteamController* controller = (ISteamController*) pointer;
		return controller->Shutdown();
	*/

	private static native void runFrame(long pointer); /*
		ISteamController* controller = (ISteamController*) pointer;
		controller->RunFrame();
	*/

	private static native int getConnectedControllers(long pointer, long[] handlesOut); /*
		ISteamController* controller = (ISteamController*) pointer;
		return controller->GetConnectedControllers((ControllerHandle_t*) handlesOut);
	*/

	private static native boolean showBindingPanel(long pointer, long controllerHandle); /*
		ISteamController* controller = (ISteamController*) pointer;
		return controller->ShowBindingPanel((ControllerHandle_t) controllerHandle);
	*/

	private static native long getActionSetHandle(long pointer, String actionSetName); /*
		ISteamController* controller = (ISteamController*) pointer;
		return controller->GetActionSetHandle(actionSetName);
	*/

	private static native void activateActionSet(long pointer, long controllerHandle, long actionSetHandle); /*
		ISteamController* controller = (ISteamController*) pointer;
		controller->ActivateActionSet((ControllerHandle_t) controllerHandle, (ControllerActionSetHandle_t) actionSetHandle);
	*/

	private static native long getCurrentActionSet(long pointer, long controllerHandle); /*
		ISteamController* controller = (ISteamController*) pointer;
		return controller->GetCurrentActionSet((ControllerHandle_t) controllerHandle);
	*/

	private static native long getDigitalActionHandle(long pointer, String actionName); /*
		ISteamController* controller = (ISteamController*) pointer;
		return controller->GetDigitalActionHandle(actionName);
	*/

	private static native void getDigitalActionData(long pointer,
													long controllerHandle,
													long digitalActionHandle,
													SteamControllerDigitalActionData digitalActionData); /*

		ISteamController* controller = (ISteamController*) pointer;
		ControllerDigitalActionData_t result = controller->GetDigitalActionData(
			(ControllerHandle_t) controllerHandle, (ControllerDigitalActionHandle_t) digitalActionHandle);

		{
			jclass clazz = env->GetObjectClass(digitalActionData);

			jfieldID field = env->GetFieldID(clazz, "state", "Z");
			env->SetBooleanField(digitalActionData, field, (jboolean) result.bState);

			field = env->GetFieldID(clazz, "active", "Z");
			env->SetBooleanField(digitalActionData, field, (jboolean) result.bActive);
		}
	*/

	private static native int getDigitalActionOrigins(long pointer,
													  long controllerHandle,
													  long actionSetHandle,
													  long digitalActionHandle,
													  int[] originsOut); /*

		ISteamController* controller = (ISteamController*) pointer;
		return controller->GetDigitalActionOrigins((ControllerHandle_t) controllerHandle,
			(ControllerActionSetHandle_t) actionSetHandle, (ControllerDigitalActionHandle_t) digitalActionHandle,
			(EControllerActionOrigin*) originsOut);
	*/

	private static native long getAnalogActionHandle(long pointer, String actionName); /*
		ISteamController* controller = (ISteamController*) pointer;
		return controller->GetAnalogActionHandle(actionName);
	*/

	private static native void getAnalogActionData(long pointer,
												   long controllerHandle,
												   long analogActionHandle,
												   SteamControllerAnalogActionData analogActionData); /*

		ISteamController* controller = (ISteamController*) pointer;
		ControllerAnalogActionData_t result = controller->GetAnalogActionData(
			(ControllerHandle_t) controllerHandle, (ControllerAnalogActionHandle_t) analogActionHandle);

		{
			jclass clazz = env->GetObjectClass(analogActionData);

			jfieldID field = env->GetFieldID(clazz, "mode", "I");
			env->SetIntField(analogActionData, field, (jint) result.eMode);

			field = env->GetFieldID(clazz, "x", "F");
			env->SetFloatField(analogActionData, field, (jfloat) result.x);

			field = env->GetFieldID(clazz, "y", "F");
			env->SetFloatField(analogActionData, field, (jfloat) result.y);

			field = env->GetFieldID(clazz, "active", "Z");
			env->SetBooleanField(analogActionData, field, (jboolean) result.bActive);
		}
	*/

	private static native int getAnalogActionOrigins(long pointer,
													 long controllerHandle,
													 long actionSetHandle,
													 long analogActionHandle,
													 int[] originsOut); /*

		ISteamController* controller = (ISteamController*) pointer;
		return controller->GetAnalogActionOrigins((ControllerHandle_t) controllerHandle,
			(ControllerActionSetHandle_t) actionSetHandle, (ControllerAnalogActionHandle_t) analogActionHandle,
			(EControllerActionOrigin*) originsOut);
	*/

	private static native void stopAnalogActionMomentum(long pointer,
														long controllerHandle,
														long analogActionHandle); /*

		ISteamController* controller = (ISteamController*) pointer;
		controller->StopAnalogActionMomentum((ControllerHandle_t) controllerHandle,
			(ControllerAnalogActionHandle_t) analogActionHandle);
	*/

	private static native void triggerHapticPulse(long pointer,
												  long controllerHandle,
												  int targetPad,
												  int durationMicroSec); /*

		ISteamController* controller = (ISteamController*) pointer;
		controller->TriggerHapticPulse((ControllerHandle_t) controllerHandle,
			(ESteamControllerPad) targetPad, (unsigned short) durationMicroSec);
	*/

	private static native void triggerRepeatedHapticPulse(long pointer,
														  long controllerHandle,
														  int targetPad,
														  int durationMicroSec,
														  int offMicroSec,
														  int repeat,
														  int flags); /*

		ISteamController* controller = (ISteamController*) pointer;
		controller->TriggerRepeatedHapticPulse((ControllerHandle_t) controllerHandle,
				(ESteamControllerPad) targetPad, (unsigned short) durationMicroSec,
				(unsigned short) offMicroSec, (unsigned short) repeat, (unsigned int) flags);
	*/

	private static native void triggerVibration(long pointer,
												long controllerHandle,
												short leftSpeed,
												short rightSpeed); /*

		ISteamController* controller = (ISteamController*) pointer;
		controller->TriggerVibration((ControllerHandle_t) controllerHandle,
			(unsigned short) leftSpeed, (unsigned short) rightSpeed);
	*/

	private static native void setLEDColor(long pointer, long controllerHandle,
										   byte colorR, byte colorG, byte colorB, int flags); /*

		ISteamController* controller = (ISteamController*) pointer;
		controller->SetLEDColor((ControllerHandle_t) controllerHandle,
			colorR, colorG, colorB, (unsigned int) flags);
	*/

	private static native int getGamepadIndexForController(long pointer, long controllerHandle); /*
		ISteamController* controller = (ISteamController*) pointer;
		return controller->GetGamepadIndexForController((ControllerHandle_t) controllerHandle);
	*/

	private static native long getControllerForGamepadIndex(long pointer, int index); /*
		ISteamController* controller = (ISteamController*) pointer;
		return controller->GetControllerForGamepadIndex(index);
	*/

	private static native void getMotionData(long pointer, long controllerHandle, float[] motionData); /*
		ISteamController* controller = (ISteamController*) pointer;
		ControllerMotionData_t data = controller->GetMotionData((ControllerHandle_t) controllerHandle);

		motionData[0] = data.rotQuatX;
		motionData[1] = data.rotQuatY;
		motionData[2] = data.rotQuatZ;
		motionData[3] = data.rotQuatW;

		motionData[4] = data.posAccelX;
		motionData[5] = data.posAccelY;
		motionData[6] = data.posAccelZ;

		motionData[7] = data.rotVelX;
		motionData[8] = data.rotVelY;
		motionData[9] = data.rotVelZ;
	*/

	private static native String getStringForActionOrigin(long pointer, int origin); /*
		ISteamController* controller = (ISteamController*) pointer;
		return env->NewStringUTF(controller->GetStringForActionOrigin((EControllerActionOrigin) origin));
	*/

	private static native String getGlyphForActionOrigin(long pointer, int origin); /*
		ISteamController* controller = (ISteamController*) pointer;
		return env->NewStringUTF(controller->GetGlyphForActionOrigin((EControllerActionOrigin) origin));
	*/

	private static native int getInputTypeForHandle(long pointer, long controllerHandle); /*
		ISteamController* controller = (ISteamController*) pointer;
		return controller->GetInputTypeForHandle((ControllerHandle_t) controllerHandle);
	*/

}
