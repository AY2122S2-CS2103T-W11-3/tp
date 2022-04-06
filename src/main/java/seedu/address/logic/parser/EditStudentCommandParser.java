package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_NO_INDEX_OR_PREFIX_PROVIDED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.logic.commands.EditStudentCommand.EditStudentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditStudentCommand object
 */
public class EditStudentCommandParser implements Parser<EditStudentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditStudentCommand
     * and returns an EditStudentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditStudentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_STUDENT_NAME, PREFIX_STUDENT_PHONE, PREFIX_STUDENT_EMAIL,
                        PREFIX_STUDENT_ADDRESS, PREFIX_STUDENT_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_NO_INDEX_OR_PREFIX_PROVIDED,
                    EditStudentCommand.MESSAGE_USAGE));
        }

        EditStudentDescriptor editStudentDescriptor = new EditStudentDescriptor();
        if (argMultimap.getValue(PREFIX_STUDENT_NAME).isPresent()) {
            editStudentDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_STUDENT_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_STUDENT_PHONE).isPresent()) {
            editStudentDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_STUDENT_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_STUDENT_EMAIL).isPresent()) {
            editStudentDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_STUDENT_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_STUDENT_ADDRESS).isPresent()) {
            editStudentDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(
                    PREFIX_STUDENT_ADDRESS).get()));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_STUDENT_TAG)).ifPresent(editStudentDescriptor::setTags);

        if (!editStudentDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditStudentCommand.MESSAGE_NOT_EDITED);
        }

        return new EditStudentCommand(index, editStudentDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
