package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ComChannelAttribute;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ComChannelType;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalAttribute;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementMetaDataAreaType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;

import java.util.List;

public class MovementFactMapper {
    public static MovementFact mapMovementFact(MovementType movement, MobileTerminalType mobileTerminal, Vessel vessel, String comChannelType) {
        MovementFact fact = new MovementFact();

        fact.setMovementMovement(movement);
        fact.setMovementGuid(movement.getGuid());

        // ROOT
        // TODO
//        fact.setAssetGroup(assetGroup);

        // ACTIVITY
        if (movement.getActivity() != null) {
            fact.setActivityCallback(movement.getActivity().getCallback());
            fact.setActivityMessageId(movement.getActivity().getMessageId());
            if (movement.getActivity().getMessageType() != null) {
                fact.setActivityMessageType(movement.getActivity().getMessageType().name());
            }
        }

        // AREA
        if (movement.getMetaData() != null) {
            List<MovementMetaDataAreaType> areas = movement.getMetaData().getAreas();
            for (MovementMetaDataAreaType area : areas) {
                fact.getAreaCodes().add(area.getCode());
                fact.getAreaNames().add(area.getName());
                fact.getAreaTypes().add(area.getAreaType());
                fact.getAreaRemoteIds().add(area.getRemoteId());
            }
        }

        // ASSET
        if (vessel != null) {
            fact.setAssetIdGearType(vessel.getGearType());
            fact.setExternalMarking(vessel.getExternalMarking());
            fact.setFlagState(vessel.getCountryCode());
            fact.setVesselCfr(vessel.getCfr());
            fact.setVesselIrcs(vessel.getIrcs());
            fact.setVesselName(vessel.getName());
            fact.setVesselGuid(vessel.getVesselId().getGuid());
        }

        // MOBILE_TERMINAL
        fact.setComChannelType(comChannelType);
        if (mobileTerminal != null) {
            fact.setMobileTerminalType(mobileTerminal.getType());
            List<ComChannelType> channels = mobileTerminal.getChannels();
            for (ComChannelType channel : channels) {
                List<ComChannelAttribute> chanAttributes = channel.getAttributes();
                for (ComChannelAttribute chanAttribute : chanAttributes) {
                    if (chanAttribute.getType().equals("DNID")) {
                        fact.setMobileTerminalDnid(chanAttribute.getValue());
                    }
                    if (chanAttribute.getType().equals("MEMBER_NUMBER")) {
                        fact.setMobileTerminalMemberNumber(chanAttribute.getValue());
                    }
                }
            }
            List<MobileTerminalAttribute> attributes = mobileTerminal.getAttributes();
            for (MobileTerminalAttribute attribute : attributes) {
                if (attribute.getType().equals("SERIAL_NUMBER")) {
                    fact.setMobileTerminalSerialNumber(attribute.getValue());
                }
            }
        }

        // POSITION
        if (movement.getPosition() != null) {
            fact.setAltitude(movement.getPosition().getAltitude());
            fact.setLatitude(movement.getPosition().getLatitude());
            fact.setLongitude(movement.getPosition().getLongitude());
        }
        fact.setCalculatedCourse(movement.getCalculatedCourse());
        fact.setCalculatedSpeed(movement.getCalculatedSpeed());
        if (movement.getMovementType() != null) {
            fact.setMovementType(movement.getMovementType().name());
        }
        if (movement.getPositionTime() != null) {
            fact.setPositionTime(movement.getPositionTime().toGregorianCalendar().getTime());
        }
        fact.setReportedCourse(movement.getReportedCourse());
        fact.setReportedSpeed(movement.getReportedSpeed());
        if (movement.getMetaData() != null) {
            if (movement.getMetaData().getFromSegmentType() != null) {
                fact.setSegmentType(movement.getMetaData().getFromSegmentType().name());
            }
            if (movement.getMetaData().getClosestCountry() != null) {
                fact.setClosestCountryCode(movement.getMetaData().getClosestCountry().getCode());
            }
            if (movement.getMetaData().getClosestPort() != null) {
                fact.setClosestPortCode(movement.getMetaData().getClosestPort().getCode());
            }
        }
        if (movement.getSource() != null) {
            fact.setSource(movement.getSource().name());
        }
        fact.setStatusCode(movement.getStatus());
        // TODO
//        fact.setVicinityOf(vicinityOf);

        return fact;
    }

}