import {
  DateField,
  DeleteButton,
  EditButton,
  List,
  ShowButton,
  useTable,
} from "@refinedev/antd";
import { type BaseRecord } from "@refinedev/core";
import { Space, Table } from "antd";

export const BookList = () => {
  const { tableProps } = useTable({
    syncWithLocation: true,
  });

  return (
    <List>
      <Table {...tableProps} rowKey="id">
        <Table.Column dataIndex="id" title={"ID"} hidden />
        <Table.Column dataIndex="isbn" title={"ISBN"} />
        <Table.Column dataIndex="title" title={"Title"} />
        <Table.Column dataIndex="authorFirstName" title={"Author's First Name"} />
        <Table.Column dataIndex="authorLastName" title={"Author's Last Name"} />
        <Table.Column dataIndex="publisherName" title={"Publisher's Name"} />
        <Table.Column dataIndex="price" title={"Price"} />
        <Table.Column dataIndex="stockQuantity" title={"Stock Quantity"} />
        <Table.Column dataIndex="status" title={"Status"} />
        <Table.Column
          dataIndex={["createdAt"]}
          title={"Created at"}
          render={(value: any) => <DateField value={value} />}
        />
        <Table.Column
          title={"Actions"}
          dataIndex="actions"
          render={(_, record: BaseRecord) => (
            <Space>
              <EditButton hideText size="small" recordItemId={record.id} />
              <ShowButton hideText size="small" recordItemId={record.id} />
              <DeleteButton hideText size="small" recordItemId={record.id} />
            </Space>
          )}
        />
      </Table>
    </List>
  );
};
