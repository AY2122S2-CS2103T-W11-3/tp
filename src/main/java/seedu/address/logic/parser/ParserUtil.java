package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.lesson.DateTimeSlot;
import seedu.address.model.lesson.LessonAddress;
import seedu.address.model.lesson.LessonName;
import seedu.address.model.lesson.Subject;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code LessonName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String lessonName} into a {@code String}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code lessonName} is invalid.
     */
    public static String parseLessonName(String lessonName) throws ParseException {
        requireNonNull(lessonName);
        String trimmedName = lessonName.trim();
        if (!LessonName.isValidName(trimmedName)) {
            throw new ParseException(LessonName.MESSAGE_CONSTRAINTS);
        }
        return trimmedName;
    }

    /**
     * Parses a {@code String subject} into a {@code String}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code subject} is invalid.
     */
    public static String parseSubject(String subject) throws ParseException {
        requireNonNull(subject);
        String trimmedSubject = subject.trim();
        if (!Subject.isValidSubject(trimmedSubject)) {
            throw new ParseException(Subject.MESSAGE_CONSTRAINTS);
        }
        return trimmedSubject;
    }

    /**
     * Parses a {@code String address} into a {@code String}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static String parseStartLessonAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!LessonAddress.isValidAddress(trimmedAddress)) {
            throw new ParseException(LessonAddress.MESSAGE_CONSTRAINTS);
        }

        return trimmedAddress;
    }

    /**
     * Parses a {@code String dateOfLesson} into a {@code LocalDate}.
     *
     * @throws ParseException if the given {@code dateOfLesson} is invalid.
     */
    public static LocalDate parseDate(String dateOfLesson) throws ParseException {
        requireNonNull(dateOfLesson);
        String trimmedDateString = dateOfLesson.trim();

        if (!DateTimeSlot.isValidDate(dateOfLesson)) {
            throw new ParseException("TODO: figure out how to better handle exception for parsing date of lesson");
        }

        LocalDate date;
        try {
            DateTimeFormatter acceptedDateTimeFormat = DateTimeSlot.getAcceptedDateFormat();
            date = LocalDate.parse(trimmedDateString, acceptedDateTimeFormat);
        } catch (DateTimeParseException exception) {
            // TODO: figure out how to better handle exception for parsing date of lesson
            throw new ParseException("TODO: figure out how to better handle exception for parsing date of lesson");
        }

        return date;
    }

    /**
     * Parses a {@code String startTime} into a {@code String}.
     *
     * @throws ParseException if the given {@code dateOfLesson} is invalid.
     */
    public static String parseStartTime(String startTime) throws ParseException {
        requireNonNull(startTime);
        String trimmedStartTimeString = startTime.trim();

        if (!DateTimeSlot.isValidStartTime(trimmedStartTimeString)) {
            throw new ParseException("TODO: figure out how to better handle exception for parsing date of lesson");
        }

        return trimmedStartTimeString;
    }

    /**
     * Parses a {@code String durationHours} into an {@code Integer}.
     *
     * @throws ParseException if the given {@code lessonDurationHours} is invalid.
     */
    public static int parseDurationHours(String durationHours) throws ParseException {
        requireNonNull(durationHours);
        String trimmedDurationString = durationHours.trim();

        Integer hours;
        try {
            hours = Integer.parseInt(trimmedDurationString);
        } catch (NumberFormatException exception) {
            // TODO: figure out how to better handle exception for parsing duration of lesson (hours)
            throw new ParseException("TODO: figure out how to better handle exception for parsing duration of lesson");
        }

        return hours;
    }

    /**
     * Parses a {@code String durationMinutes} into an {@code Integer}.
     *
     * @throws ParseException if the given {@code lessonDurationMinutes} is invalid.
     */
    public static int parseDurationMinutes(String durationMinutes) throws ParseException {
        requireNonNull(durationMinutes);
        String trimmedDurationString = durationMinutes.trim();

        Integer minutes;
        try {
            minutes = Integer.parseInt(trimmedDurationString);
        } catch (NumberFormatException exception) {
            // TODO: figure out how to better handle exception for parsing duration of lesson (minutes)
            throw new ParseException("TODO: figure out how to better handle exception for parsing duration of lesson");
        }

        return minutes;
    }
}
